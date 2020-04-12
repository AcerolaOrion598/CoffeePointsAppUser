package com.djaphar.coffeepointappuser.SupportClasses.ApiClasses;

public class SupervisorModel {

    private String _id, name;
    private Float avgRating;

    public SupervisorModel(String _id, String name, Float avgRating) {
        this._id = _id;
        this.name = name;
        this.avgRating = avgRating;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public Float getAvgRating() {
        return avgRating;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvgRating(Float avgRating) {
        this.avgRating = avgRating;
    }
}
