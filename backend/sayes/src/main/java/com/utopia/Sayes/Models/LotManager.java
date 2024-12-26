package com.utopia.Sayes.Models;

public class LotManager extends User{
    private long revenue;
    public LotManager(String username, String user_password , long revenue) {
        super(username, user_password);
        this.revenue = revenue;
    }



    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }
}
