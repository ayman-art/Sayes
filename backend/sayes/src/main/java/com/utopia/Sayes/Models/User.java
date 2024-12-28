package com.utopia.Sayes.Models;

public class User {
    protected long user_id;
    private String username;
    private String user_password;

    public User(String username , String user_password) {
        this.username = username;
        this.user_password = user_password;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }
}
