package com.djaphar.coffeepointappuser.SupportClasses.ApiClasses;

public class SupervisorModel {

    private String _id, name;
    private Float rating;

    public SupervisorModel(String _id, String name, Float rating) {
        this._id = _id;
        this.name = name;
        this.rating = rating;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public Float getRating() {
        return rating;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
