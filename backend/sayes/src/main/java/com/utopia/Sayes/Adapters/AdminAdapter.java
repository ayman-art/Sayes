package com.utopia.Sayes.Adapters;

import com.google.gson.Gson;
import com.utopia.Sayes.Models.Admin;

import java.util.HashMap;
import java.util.Map;

public class AdminAdapter implements IAdapter<Admin> {
    @Override
    public Map<String, Object> toMap(Admin admin) {
        Map<String, Object> AdminMap = new HashMap<>();
        AdminMap.put("username", admin.getUsername());
        AdminMap.put("user_password",admin.getUser_password());
        return AdminMap;
    }

    @Override
    public Admin fromMap(Map<String, Object> map) {
        String username = (String) map.get("username");
        String user_password = (String) map.get("user_password");
        Admin admin = new Admin(username , user_password);
        admin.setAdmin_id((Long) map.get("Admin_id"));
        return admin;
    }

    @Override
    public String toJson(Admin admin) {
        Gson gson = new Gson();
        return gson.toJson(admin);
    }
}
