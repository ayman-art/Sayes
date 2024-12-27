import json
import random
import requests
from helpers import CREATE_LOT_ENDPOINT, LOT_MANAGER_TOKEN_FILE, LOTS_FILE, SIGNUP_ENDPOINT, random_string


def register_lot_manager():
    """Register a lot manager user and save their JWT token."""
    username = random_string(8)
    password = random_string(12)
    
    # Make sign-up request
    response = requests.post(SIGNUP_ENDPOINT, json={
        "name": username,
        "password": password,
        "role": "lot-manager"
    })
    
    if response.status_code == 200:
        jwt_token = response.json().get("jwt")
        if jwt_token:
            # Save the token to a file
            with open(LOT_MANAGER_TOKEN_FILE, "w") as f:
                json.dump({"token": jwt_token}, f)
            print(f"Lot manager created: {username}")
        else:
            print("Failed to retrieve lot manager token.")
    else:
        print(f"Sign-up failed: {response.status_code}, {response.text}")
    


register_lot_manager()