
import json

import requests
from helpers import SIGNUP_ENDPOINT, TOKENS_FILE, random_string, random_string_numbers


def sign_up_and_save_tokens(num_accounts=100):
    """Sign up multiple accounts and save JWT tokens to a file."""
    tokens = []
    for _ in range(num_accounts):
        username = random_string(8)
        password = random_string(12)
        plate = random_string_numbers(8)
        number = random_string_numbers(8)
        headers = {"Content-Type": "application/json"}
        response = requests.post(SIGNUP_ENDPOINT, headers=headers, json={
            "name": username,
            "password": password,
            "plateNumber" : plate,
	        "licenseNumber" : number,
			"role": "driver"
        })
        
        if response.status_code == 200:
            jwt_token = response.json().get("jwt")
            if jwt_token:
                tokens.append(jwt_token)
                print(f"Account created: {username}")
            else:
                print("Failed to retrieve token.")
        else:
            print(f"Sign-up failed: {response.status_code}, {response.text}")
    
    # Save tokens to file
    with open(TOKENS_FILE, "w") as f:
        json.dump(tokens, f)
    print(f"Saved {len(tokens)} tokens to {TOKENS_FILE}")

sign_up_and_save_tokens(num_accounts=100)