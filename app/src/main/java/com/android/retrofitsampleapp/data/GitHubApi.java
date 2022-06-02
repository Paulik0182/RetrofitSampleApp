package com.android.retrofitsampleapp.data;

import com.android.retrofitsampleapp.domain.project.GitProjectEntity;
import com.android.retrofitsampleapp.domain.users.GitUserEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubApi {

    @GET("users")
    Call<List<GitUserEntity>> getUsers();

    @GET("users/{user}/repos")
    Call<List<GitProjectEntity>> getProject(@Path("user") String user);

    @GET("users/{user}")
    Call<GitUserEntity> getUser(@Path("user") String user);
}
