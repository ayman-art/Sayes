package com.utopia.Sayes.Models;

public class Admin extends User{
    private long Admin_id;
    public Admin(String username, String user_password) {
        super(username, user_password);
        this.Admin_id = super.getUser_id();
    }

    public long getAdmin_id() {
        return Admin_id;
    }

    public void setAdmin_id(long admin_id) {
        Admin_id = admin_id;
    }
}
