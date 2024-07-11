import warnings
warnings.simplefilter(action='ignore', category=FutureWarning)
import sys
import torch
from transformers import AutoModel, AutoTokenizer
from pyvi import ViTokenizer
from qdrant_client import QdrantClient, models
import io

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

QDRANT_URL = "http://localhost:6333"
client = QdrantClient(url=QDRANT_URL)

def des_to_vector(des):
    model_name = "VoVanPhuc/sup-SimCSE-VietNamese-phobert-base"
    tokenizer = AutoTokenizer.from_pretrained(model_name)
    model = AutoModel.from_pretrained(model_name)

    # Tokenize description
    tokenized_des = ViTokenizer.tokenize(des)
    inputs = tokenizer([tokenized_des], padding=True, truncation=True, return_tensors="pt")

    # Convert description to vector
    with torch.no_grad():
        embeddings = model(**inputs, output_hidden_states=True, return_dict=True).pooler_output

    return embeddings.cpu().numpy()[0].tolist()


def search_similar_vectors(collection_name, query_vector, limit, score_threshold):
    try:
        # Use the global client variable
        global client
        result = client.search(
            collection_name=collection_name,
            query_vector=query_vector,
            with_vectors=False,
            with_payload=True,
            score_threshold=score_threshold,
            limit=limit
        )
        return result
    except Exception as e:
        print("false")
        return None

def get_isbns_from_results(results):
    """Extract and return ISBNs from the search results."""
    if not results:
        return ""

    isbns = []
    for result in results:
        try:
            isbn = result.payload.get("isbn", "Unknown ISBN")
            isbns.append(isbn)
        except Exception as e:
            print("false")

    return "\n".join(isbns)

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("false")
        sys.exit(1)

    collection_name = sys.argv[1]
    des = sys.argv[2]
    # Convert description to vector
    query_vector = des_to_vector(des)

    # Save data to Qdrant
    results = search_similar_vectors(collection_name, query_vector, 6, 0.5)
    isbns = get_isbns_from_results(results)
    print(isbns)
