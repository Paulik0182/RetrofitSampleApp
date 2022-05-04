package com.android.retrofitsampleapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubApi {

    @GET("users")
    Call<List<GitUserEntity>> getUsers();

    @GET("users/{user}/repos")
    Call<List<GitProjectEntity>> getProject(@Path("user") String user);
}
