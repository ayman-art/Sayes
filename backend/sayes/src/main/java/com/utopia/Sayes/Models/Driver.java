package com.utopia.Sayes.Models;

public class Driver extends User{
    private long Driver_id;
    private String plate_number;
    private long balance;
    private String payment_method;
    private long license_number;
    public Driver(String username, String user_password , long balance, String plate_number , long license_number) {
        super(username, user_password);
        this.Driver_id = super.getUser_id();
        this.balance = balance;
        this.license_number = license_number;
        this.plate_number = plate_number;
    }

    public long getDriver_id() {
        return Driver_id;
    }

    public void setDriver_id(long driver_id) {
        Driver_id = driver_id;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public long getLicense_number() {
        return license_number;
    }

    public void setLicense_number(long license_number) {
        this.license_number = license_number;
    }
}
