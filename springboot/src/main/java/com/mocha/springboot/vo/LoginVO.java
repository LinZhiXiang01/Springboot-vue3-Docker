package com.mocha.springboot.vo;

import com.mocha.springboot.entity.EmployeeProfile;

public class LoginVO {
    private Integer id;
    private String username;
    private String role;
    private String token;
    private EmployeeProfile profile; // 如果你希望带上员工的完整资料，可嵌套

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public EmployeeProfile getProfile() {
        return profile;
    }

    public void setProfile(EmployeeProfile profile) {
        this.profile = profile;
    }
}
