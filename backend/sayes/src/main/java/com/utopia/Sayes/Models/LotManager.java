package com.utopia.Sayes.Models;

public class LotManager extends User{
    private long manager_id;
    private long revenue;
    public LotManager(String username, String user_password , long revenue) {
        super(username, user_password);
        this.manager_id = super.getUser_id();
        this.revenue = revenue;
    }

    public long getManager_id() {
        return manager_id;
    }

    public void setManager_id(long manager_id) {
        this.manager_id = manager_id;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }
}
