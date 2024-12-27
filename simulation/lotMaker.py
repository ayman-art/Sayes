import json
import random

import requests

from helpers import CREATE_LOT_ENDPOINT, LOT_MANAGER_TOKEN_FILE, LOTS_FILE, random_string


def random_location(lat_range, lon_range):
    latitude = random.uniform(lat_range[0], lat_range[1])
    longitude = random.uniform(lon_range[0], lon_range[1])
    return {"latitude": latitude, "longitude": longitude}

def create_lots(num_lots=100, lat_range=(30.0, 31.0), lon_range=(29.0, 30.0)):
    with open(LOT_MANAGER_TOKEN_FILE, "r") as f:
        jwt_token = json.load(f).get("token")
    
    headers = {"Authorization": f"Bearer {jwt_token}"}
    lots = []  

    for _ in range(num_lots):
        location = random_location(lat_range, lon_range)
        data = {
            "longitude": location['longitude'],
            "latitude": location['latitude'],
            "revenue": 0,
            "price": 50,
            "num_of_spots" : 5,
            "lot_type": "Public",
            "penalty": 10.5,
            "fee": 5.5,
            "time": "PT1M"
        }
        response = requests.post(CREATE_LOT_ENDPOINT, headers=headers, json=data)
        if response.status_code == 200:
            lot = response.json()
            lots.append(lot)
            print(f"Lot created: {lot}")
        else:
            print(f"Failed to create lot: {response.status_code}, {response.text}")

    with open(LOTS_FILE, "w") as f:
        json.dump(lots, f)
    print(f"Saved {len(lots)} lots to {LOTS_FILE}")

create_lots()