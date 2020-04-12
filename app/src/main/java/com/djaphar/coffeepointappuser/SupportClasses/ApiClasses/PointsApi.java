package com.djaphar.coffeepointappuser.SupportClasses.ApiClasses;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PointsApi {

    @GET("api/couriers")
    Call<ArrayList<Point>> requestPointsInBox(@Query("box") String box);

    @GET("api/supervisors/{id}")
    Call<SupervisorModel> requestSupervisor(@Path("id") String id);

    @POST("api/users/handshake")
    Call<User> requestUser(@Body AuthModel authModel);

    @POST("api/couriers/{id}/reviews")
    Call<Void> requestSetCourierReview(@Path("id") String id, @HeaderMap Map<String, String> headers, @Body ReviewModel reviewModel);

    @POST("api/supervisors/{id}/reviews")
    Call<Void> requestSetSupervisorReview(@Path("id") String id, @HeaderMap Map<String, String> headers, @Body ReviewModel reviewModel);
}
