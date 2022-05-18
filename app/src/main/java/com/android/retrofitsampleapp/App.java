package com.android.retrofitsampleapp;

import android.app.Application;

import com.android.retrofitsampleapp.data.GitHubApi;
import com.android.retrofitsampleapp.data.RetrofitGitUsersRepoImpl;
import com.android.retrofitsampleapp.domain.GitUsersRepo;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    //увеличили время по таймауту при загрузке из сети (без этого, по умолчанию 10сек.)
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(UsedConst.settingTimeConst.ITEM_OUT_CONST, TimeUnit.SECONDS)
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(UsedConst.httpsConst.GIT_CONST)
            .client(client)// увеличение времени по таймауту
            .addConverterFactory(GsonConverterFactory.create())// это приобразователь объектов из одного типа в другой тип (здесь старонняя библиотека)
            .build();

    private final GitHubApi gitHubApi = retrofit.create(GitHubApi.class); //создаем gitHubApi. Автоматически обратится к интерфейсу


    private GitUsersRepo gitUsersRepo;

    //context доступен после вызова onCreate, поэтому заводим данный метод
    @Override
    public void onCreate() {
        super.onCreate();

        //конкретная реализация RepoImpl
        gitUsersRepo = new RetrofitGitUsersRepoImpl(this, gitHubApi); //отдали в метод GitUsersRepo
    }

    public GitHubApi getGitHubApi() {
        return gitHubApi;
    }

    public GitUsersRepo getUsersRepo() {
        return gitUsersRepo;
    }
}
