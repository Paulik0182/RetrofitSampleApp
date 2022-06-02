package com.android.retrofitsampleapp;

import android.app.Application;

import com.android.retrofitsampleapp.data.GitHubApi;
import com.android.retrofitsampleapp.data.project.CachedNetworkProjectRepoImpl;
import com.android.retrofitsampleapp.data.project.RetrofitGitProjectRepoImpl;
import com.android.retrofitsampleapp.data.project.SnappyDbGitProjectRepoImpl;
import com.android.retrofitsampleapp.data.users.CachedNetworkUsersRepoImpl;
import com.android.retrofitsampleapp.data.users.RetrofitGitUsersRepoImpl;
import com.android.retrofitsampleapp.data.users.SnappyDbGitUsersRepoImpl;
import com.android.retrofitsampleapp.domain.project.GitProjectRepo;
import com.android.retrofitsampleapp.domain.users.GitUsersRepo;

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
    private GitProjectRepo gitProjectRepo;

    //context доступен после вызова onCreate, поэтому заводим данный метод
    @Override
    public void onCreate() {
        super.onCreate();
        GitUsersRepo UsersLocalRepo = new SnappyDbGitUsersRepoImpl(this);
        GitUsersRepo networkUsersRepo = new RetrofitGitUsersRepoImpl(this, gitHubApi);

        GitProjectRepo ProjectLocalRepo = new SnappyDbGitProjectRepoImpl(this);
        GitProjectRepo networkProjectsRepo = new RetrofitGitProjectRepoImpl(this, gitHubApi);

        //конкретная реализация RepoImpl
        gitUsersRepo = new CachedNetworkUsersRepoImpl(networkUsersRepo, UsersLocalRepo); //отдали в метод GitUsersRepo
        gitProjectRepo = new CachedNetworkProjectRepoImpl(networkProjectsRepo, ProjectLocalRepo); //отдали в метод GitProjectRepo
    }

    public GitHubApi getGitHubApi() {
        return gitHubApi;
    }

    public GitUsersRepo getUsersRepo() {
        return gitUsersRepo;
    }

    public GitProjectRepo getProjectRepo() {
        return gitProjectRepo;
    }
}
