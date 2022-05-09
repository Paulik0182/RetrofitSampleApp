package com.android.retrofitsampleapp.ui.users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.retrofitsampleapp.R;
import com.android.retrofitsampleapp.data.GitHubApi;
import com.android.retrofitsampleapp.domain.GitUserEntity;
import com.android.retrofitsampleapp.ui.common.BaseActivity;
import com.android.retrofitsampleapp.ui.projects.ProjectsActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersActivity extends BaseActivity {

    private GitHubApi gitHubApi;//достаем из класса App из метода GitHubApi -> gitHubApi

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private final GitUsersAdapter adapter = new GitUsersAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        gitHubApi = getApp().getGitHubApi();//достаем из класса App из метода GitHubApi -> gitHubApi.
        // при этом getApp() берется в общем классе BaseActivity, ткак-как от этого класса мы наследуемся

        initView();

        adapter.setOnItemClickListener(this::openUserScreen);// ::это ссылка на один метод,
        // а :: означает, что этот метод использовать как лямду чтобы передать его в адаптер
        // и приобразовать его в OnItemClickListener (это синтаксический сахр)

        //второй вариант написания
//        adapter.setOnItemClickListener(user ->{
//            openUserScreen(user);
//        });

        loadUsers();
    }

    private void loadUsers() {
        showProgress(true);
        gitHubApi.getUsers().enqueue(new Callback<List<GitUserEntity>>() {
            //получение ответа
            @Override
            public void onResponse(Call<List<GitUserEntity>> call, Response<List<GitUserEntity>> response) {
                showProgress(false);
//                if (response.code()==200){  //проверка кода, код 200 означает успех
                if (response.isSuccessful()) { //isSuccessful - это уже проверка кодов от 200 до 300
                    List<GitUserEntity> users = response.body(); // body - это тело запроса, это будет список репозиториев которые мы ищем. Здесь мы получаем список проектов

                    adapter.setData(users);
                    //test
                    Toast.makeText(UsersActivity.this, "Size" + users.size(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(UsersActivity.this, "Error code" + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            //получение ошибки. чтото сломалось (нет сети и т.д.)
            @Override
            public void onFailure(Call<List<GitUserEntity>> call, Throwable t) {
                showProgress(false);
                Toast.makeText(UsersActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openUserScreen(GitUserEntity user) {
        Intent intent = ProjectsActivity.getLaunchIntent(this, user.getLogin());
        startActivity(intent);
        Toast.makeText(this, "Нажали " + user.getLogin(), Toast.LENGTH_SHORT).show();
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