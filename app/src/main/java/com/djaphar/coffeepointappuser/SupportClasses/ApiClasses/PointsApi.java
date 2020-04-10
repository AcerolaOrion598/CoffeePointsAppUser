package com.djaphar.coffeepointappuser.SupportClasses.ApiClasses;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PointsApi {

    @GET("api/couriers")
    Call<ArrayList<Point>> requestPointsInBox(@Query("box") String box);

    @GET("api/supervisors/{id}")
    Call<SupervisorModel> requestSupervisor(@Path("id") String id);
}
