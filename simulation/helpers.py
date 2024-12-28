import random
import string


BASE_URL = "http://localhost:8080"  
SIGNUP_ENDPOINT = f"{BASE_URL}/users/register"
RESERVATION_ENDPOINT = f"{BASE_URL}/reservation/reserve-spot"
CREATE_LOT_ENDPOINT = f"{BASE_URL}/lot-management/create-lot"
LOT_MANAGER_TOKEN_FILE = "lot_manager_token.json"
LOTS_FILE = "lots.json"
TOKENS_FILE = "tokens.json"

def random_string(length):
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

def random_string_numbers(length):
    return ''.join(random.choices(string.digits, k = length))