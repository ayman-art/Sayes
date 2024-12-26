package com.utopia.Sayes.Models;

public class Admin extends User{
    public Admin(String username, String user_password) {
        super(username, user_password);
        this.user_id = super.getUser_id();
    }

}
