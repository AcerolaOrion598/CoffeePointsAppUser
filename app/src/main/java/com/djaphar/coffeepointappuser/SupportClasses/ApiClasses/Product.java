package com.djaphar.coffeepointappuser.SupportClasses.ApiClasses;

public class Product {

    private String _id, type, name, supervisor;

    public Product(String _id, String type, String name, String supervisor) {
        this._id = _id;
        this.type = type;
        this.name = name;
        this.supervisor = supervisor;
    }

    public String get_id() {
        return _id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }
}
