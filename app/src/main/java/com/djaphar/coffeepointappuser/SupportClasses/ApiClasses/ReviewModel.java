package com.djaphar.coffeepointappuser.SupportClasses.ApiClasses;

public class ReviewModel {

    private Integer rating;

    public ReviewModel(Integer rating) {
        this.rating = rating;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
