package com.pucmm.csti.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface APIService {

    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("posts/{postId}/comments")
    Call<List<Comment>> getCommentsByPost(@Path("postId") int postId);

    @POST("posts")
    Call<Post> create(@Body Post post);
}
