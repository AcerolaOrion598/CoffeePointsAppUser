package com.djaphar.coffeepointappuser.SupportClasses.ApiClasses;

public class AuthModel {
    private String deviceId;

    public AuthModel(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
