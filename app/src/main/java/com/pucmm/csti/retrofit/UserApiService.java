package com.pucmm.csti.retrofit;

import com.google.gson.JsonObject;
import com.pucmm.csti.model.Userr;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

import java.util.List;

public interface UserApiService {

    @GET("users")
    Call<List<Userr>> getAll();

    @PUT("users")
    Call<Userr> update(@Body Userr user);

    @POST("users")
    Call<Userr> create(@Body Userr user);

    @PUT("users/change")
    Call<Void> changePassword(@Body Userr user);

    @POST("users/login")
    Call<Userr> login(@Body JsonObject user);
}
