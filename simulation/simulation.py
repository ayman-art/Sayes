from datetime import datetime, timedelta
import requests
import random
import string
import json
import time
import threading

from helpers import LOTS_FILE, RESERVATION_ENDPOINT, TOKENS_FILE


def simulate_reservations():
    """Simulate reservations using saved lots."""
    with open(TOKENS_FILE, "r") as token_file:
        tokens = json.load(token_file)
    
    with open(LOTS_FILE, "r") as lot_file:
        lots = json.load(lot_file)
    
    while True:
        # Pick a random token and lot
        jwt_token = random.choice(tokens)
        lot_id = random.choice(lots)["lotId"]
        
        make_reservation(jwt_token, lot_id)
        
        # Random delay between reservations
        delay = random.uniform(1, 5)
        time.sleep(delay)

def make_reservation(jwt_token, lot_id):
    """Make a reservation request using a JWT token and lot ID."""
    headers = {"Authorization": f"Bearer {jwt_token}"}
    now = datetime.now()
    next_minute = (now + timedelta(minutes=1)).replace(second=0, microsecond=0)
    formatted_time = next_minute.strftime("%H:%M:%S")

    data = {"lotId": lot_id, "endTime": formatted_time}  # Example payload
    response = requests.put(RESERVATION_ENDPOINT, headers=headers, json=data)
    if response.status_code == 200:
        print(f"Reservation successful: {response.json()}")
    else:
        print(f"Reservation failed: {response.status_code}, {response.text}")


def start_reservation_simulation(num_threads=10):
    """Start the reservation simulation using multiple threads."""
    threads = []
    for _ in range(num_threads):
        t = threading.Thread(target=simulate_reservations)
        t.start()
        threads.append(t)
    print(f"Started {num_threads} reservation simulation threads.")


