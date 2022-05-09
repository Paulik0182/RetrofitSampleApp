package com.android.retrofitsampleapp.ui.projects;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.retrofitsampleapp.R;
import com.android.retrofitsampleapp.data.GitHubApi;
import com.android.retrofitsampleapp.domain.GitProjectEntity;
import com.android.retrofitsampleapp.ui.common.BaseActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectsActivity extends BaseActivity {

    private static final String LOGIN_EXTRA_KEY = "LOGIN_EXTRA_KEY";

    private GitHubApi gitHubApi;//достаем из класса App из метода GitHubApi -> gitHubApi

    private final GitProjectAdapter adapter = new GitProjectAdapter();
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    public static Intent getLaunchIntent(Context context, String login) {
        Intent intent = new Intent(context, ProjectsActivity.class);
        intent.putExtra(LOGIN_EXTRA_KEY, login);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        gitHubApi = app.getGitHubApi();//достаем из класса App из метода GitHubApi -> gitHubApi.
        // при этом арр берется в общем классе BaseActivity, ткак-как от этого класса мы наследуемся

        final String login = getIntent().getStringExtra(LOGIN_EXTRA_KEY);//получаем логин
//        Toast.makeText(this, login, Toast.LENGTH_SHORT).show();

        initView();

        setTitle(login);//подставили имя в заголовок (не понял как. пояснение.)????

//        adapter.setOnItemClickListener(this::openUserScreen);// ::это ссылка на один метод,
        // а :: означает, что этот метод использовать как лямду чтобы передать его в адаптер
        // и приобразовать его в OnItemClickListener (это синтаксический сахр)

        //второй вариант написания
//        adapter.setOnItemClickListener(user ->{
//            openUserScreen(user);
//        });

        loadProjects(login);
    }

    private void loadProjects(String login) {
        showProgress(true);
        gitHubApi.getProject(login).enqueue(new Callback<List<GitProjectEntity>>() {
            //получение ответа
            @Override
            public void onResponse(Call<List<GitProjectEntity>> call, Response<List<GitProjectEntity>> response) {
                showProgress(false);
                if (response.isSuccessful()) { //isSuccessful - это уже проверка кодов от 200 до 300
                    List<GitProjectEntity> users = response.body(); // body - это тело запроса, это будет список репозиториев которые мы ищем. Здесь мы получаем список проектов

                    adapter.setData(users);
                    //test
                    Toast.makeText(ProjectsActivity.this, "Size" + users.size(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ProjectsActivity.this, "Error code" + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            //получение ошибки. чтото сломалось (нет сети и т.д.)
            @Override
            public void onFailure(Call<List<GitProjectEntity>> call, Throwable t) {
                showProgress(false);
                Toast.makeText(ProjectsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView() {
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void showProgress(boolean shouldShow) {
        if (shouldShow) {
            recyclerView.setVisibility(View.GONE);//скрываем view со списком
            progressBar.setVisibility(View.VISIBLE);//показываем прогресс загрузки
        } else {
            recyclerView.setVisibility(View.VISIBLE);//показываем view со списком
            progressBar.setVisibility(View.GONE);//скрываем прогресс загрузки
        }
    }
}
