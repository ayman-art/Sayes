package com.utopia.Sayes.Adapters;

import com.utopia.Sayes.Models.Admin;

import java.util.HashMap;
import java.util.Map;

public class AdminAdapter implements IAdapter<Admin> {
    @Override
    public Map<String, Object> toMap(Admin admin) {
        Map<String, Object> AdminMap = new HashMap<>();
        AdminMap.put("username", admin.getUsername());
        return AdminMap;
    }

    @Override
    public Admin fromMap(Map<String, Object> map) {
        String username = (String) map.get("username");
        String user_password = (String) map.get("user_password");
        Admin admin = new Admin(username , user_password);
        admin.setUser_id((Long) map.get("Admin_id"));
        return admin;
    }
}
