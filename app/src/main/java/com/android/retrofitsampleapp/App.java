package com.android.retrofitsampleapp;

import android.app.Application;

import com.android.retrofitsampleapp.data.GitHubApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

//    public static final String LOGIN_EXTRA_KEY = "LOGIN_EXTRA_KEY";
//    public static final String HTTPS_GIT_CONST = "https://api.github.com/";
//    private static final int ITEM_OUT_CONST = 15;

    //увеличили время по таймауту при загрузке из сети (без этого, по умолчанию 10сек.)
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(UsedConst.ITEM_OUT_CONST, TimeUnit.SECONDS)
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(UsedConst.HTTPS_GIT_CONST)
            .client(client)// увеличение времени по таймауту
            .addConverterFactory(GsonConverterFactory.create())// это приобразователь объектов из одного типа в другой тип (здесь старонняя библиотека)
            .build();

    private final GitHubApi gitHubApi = retrofit.create(GitHubApi.class); //создаем gitHubApi. Автоматически обратится к интерфейсу

    public GitHubApi getGitHubApi() {
        return gitHubApi;
    }
}
