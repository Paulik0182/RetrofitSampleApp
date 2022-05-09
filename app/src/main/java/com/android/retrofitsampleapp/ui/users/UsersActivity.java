package com.android.retrofitsampleapp.ui.users;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.retrofitsampleapp.R;
import com.android.retrofitsampleapp.domain.GitUserEntity;
import com.android.retrofitsampleapp.ui.git_common.BaseGitListActivity;
import com.android.retrofitsampleapp.ui.projects.ProjectsActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersActivity extends BaseGitListActivity {

    private final GitUsersAdapter adapter = new GitUsersAdapter();

    // а почему у Вас в коде onCreate -> public, а на protected
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        initView();
        setContractViews(progressBar, recyclerView);

        adapter.setOnItemClickListener(this::openUserScreen);// ::это ссылка на один метод,
        // а :: означает, что этот метод использовать как лямду чтобы передать его в адаптер
        // и приобразовать его в OnItemClickListener (это синтаксический сахр)

        loadUsers();
    }

    private void loadUsers() {
        showProgress(true);
        getGitHubApi().getUsers().enqueue(new Callback<List<GitUserEntity>>() {
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
        recyclerView.setAdapter(adapter);
    }
}