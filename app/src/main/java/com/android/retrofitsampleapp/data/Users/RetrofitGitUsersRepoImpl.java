package com.android.retrofitsampleapp.data.Users;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.retrofitsampleapp.data.GitHubApi;
import com.android.retrofitsampleapp.domain.users.GitUserEntity;
import com.android.retrofitsampleapp.domain.users.GitUsersRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class RetrofitGitUsersRepoImpl implements GitUsersRepo {

    private final Context context;

    //Для похода в интернет нам нужно GitHubApi
    private final GitHubApi gitHubApi;

    //конструктор. для получения Api
    public RetrofitGitUsersRepoImpl(Context context, GitHubApi gitHubApi) {
        this.context = context;
        this.gitHubApi = gitHubApi;
    }

    @Override
    public void getUsers(Callback callback) {
        //здесь должна быть условная проверка Callback
        gitHubApi.getUsers().enqueue(new retrofit2.Callback<List<GitUserEntity>>() {
            @Override
            public void onResponse(@NonNull Call<List<GitUserEntity>> call, @NonNull Response<List<GitUserEntity>> response) {
                if (response.isSuccessful()) { //isSuccessful - это уже проверка кодов от 200 до 300
                    callback.onSuccess(response.body());// передали список

                } else {
                    Toast.makeText(context, "Error code" + response.code(), Toast.LENGTH_SHORT).show();
                    callback.onError(new Throwable("Error code = " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GitUserEntity>> call, @NonNull Throwable t) {
                callback.onError(t);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
