# import sys
# from qdrant_client.models import Distance, VectorParams
# from qdrant_client import QdrantClient
#
# client = QdrantClient(url="http://localhost:6333")
#
# def create_collection(collection_name):
#     try:
#         # Create a collection with specific vector configuration
#         client.create_collection(
#             collection_name=collection_name,
#             vectors_config=VectorParams(size=384, distance=Distance.EUCLID),
#         )
#         print("true")
#     except Exception as e:
#         # Catch and print any exceptions that occur during client creation or collection creation
#         print("false")
#
# if __name__ == "__main__":
#     if len(sys.argv) != 2:
#         print("false")
#     else:
#         create_collection(sys.argv[1])
import warnings
warnings.simplefilter(action='ignore', category=FutureWarning)
import sys
from qdrant_client import QdrantClient
from qdrant_client.models import Distance, VectorParams

QDRANT_URL = "http://localhost:6333"
client = QdrantClient(url=QDRANT_URL)

def create_collection(collection_name):
    try:
        # Create a collection with specific vector configuration
        client.create_collection(
            collection_name=collection_name,
            vectors_config=VectorParams(size=768, distance=Distance.COSINE),
        )
        print("true")
    except Exception as e:
        # Catch and print any exceptions that occur during client creation or collection creation
        print("false")

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("false")
        sys.exit(1)

    collection_name = sys.argv[1]
    create_collection(collection_name)






