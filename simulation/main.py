from driverMaker import sign_up_and_save_tokens
from lotMaker import create_lots
from lotManagerMaker import register_lot_manager
from simulation import simulate_reservations, start_reservation_simulation


if __name__ == "__main__":
    register_lot_manager()
    create_lots()
    sign_up_and_save_tokens(num_accounts=100)
    start_reservation_simulation(10)    