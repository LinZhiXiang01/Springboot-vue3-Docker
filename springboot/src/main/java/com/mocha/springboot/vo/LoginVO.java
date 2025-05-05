package com.mocha.springboot.vo;

import com.mocha.springboot.entity.EmployeeProfile;

public class LoginVO {
    private Integer id; //实际为authId
    private String username;
    private String role;
    private String accessToken;
    private String refreshToken;
    private EmployeeProfile profile; // 如果希望带上员工的完整资料，可嵌套

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public EmployeeProfile getProfile() {
        return profile;
    }

    public void setProfile(EmployeeProfile profile) {
        this.profile = profile;
    }


}
