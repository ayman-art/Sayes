import csv
import random
import string

def generate_unique_string(length):
    """Generate a random alphanumeric string of a given length."""
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

def generate_license_number(length):
    """Generate a random license number with only digits and not starting with 0."""
    first_digit = random.randint(1, 9) 
    remaining_digits = ''.join(random.choices(string.digits, k=length - 1))
    return f"{first_digit}{remaining_digits}"

def generate_data(num_records, output_file):
    roles = ["driver", "lot-manager"]
    
    with open(output_file, mode='w', newline='') as file:
        writer = csv.writer(file)
        # Write header
        writer.writerow(["name", "password", "plateNumber", "licenseNumber", "role"])
        
        for i in range(num_records):
            name = f"User{generate_unique_string(7)}"
            password = generate_unique_string(7)
            plate_number = f"PLATE{generate_unique_string(7)}"
            license_number = generate_license_number(7)
            role = random.choice(roles)
            
            writer.writerow([name, password, plate_number, license_number, role])

generate_data(1000, "data.csv")
