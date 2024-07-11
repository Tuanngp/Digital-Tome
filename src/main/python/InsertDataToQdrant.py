import warnings
warnings.simplefilter(action='ignore', category=FutureWarning)
import sys
import torch
from transformers import AutoModel, AutoTokenizer
from pyvi import ViTokenizer
from qdrant_client import QdrantClient, models
from qdrant_client.models import Distance, VectorParams
import uuid
import json

# Qdrant API endpoint
QDRANT_URL = "http://localhost:6333"

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

def save_to_qdrant(collection_name, isbn, name, des, des_vector):
    client = QdrantClient(url=QDRANT_URL)

    try:
        client.upsert(
            collection_name=collection_name,
            points=[
                models.PointStruct(
                    id=uuid.uuid4().hex,
                    vector=des_vector,
                    payload={
                        "isbn": isbn,
                        "name": name,
                        "des": des
                    }
                )
            ]
        )
        print("true")
    except Exception as e:
        print("false")

# Main function to execute tasks based on command-line arguments
if __name__ == "__main__":
    if len(sys.argv) != 5:
        print("false")
        sys.exit(1)

    collection_name = sys.argv[1]
    isbn = sys.argv[2]
    name = sys.argv[3]
    des = sys.argv[4]

    # Convert description to vector
    des_vector = des_to_vector(des)

    # Save data to Qdrant
    save_to_qdrant(collection_name, isbn, name, des, des_vector)

