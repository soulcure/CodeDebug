package com.example.code.login.model;

public class LoginReq {
    /**
     * token : 1234
     * systemType : 1
     */

    private String token;
    private int systemType;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getSystemType() {
        return systemType;
    }

    public void setSystemType(int systemType) {
        this.systemType = systemType;
    }
}
