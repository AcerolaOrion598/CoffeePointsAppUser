package com.djaphar.coffeepointappuser.SupportClasses.ApiClasses;

public class User {

    private String _id, token;

    public User(String _id, String token) {
        this._id = _id;
        this.token = token;
    }

    public String get_id() {
        return _id;
    }

    public String getToken() {
        return token;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
