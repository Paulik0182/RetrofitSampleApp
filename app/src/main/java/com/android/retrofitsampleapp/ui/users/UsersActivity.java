package com.android.retrofitsampleapp.ui.users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.retrofitsampleapp.R;
import com.android.retrofitsampleapp.domain.users.GitUserEntity;
import com.android.retrofitsampleapp.domain.users.GitUsersRepo;
import com.android.retrofitsampleapp.ui.common.BaseActivity;
import com.android.retrofitsampleapp.ui.projects.ProjectsActivity;

import java.util.List;

public class UsersActivity extends BaseActivity {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private Button loadButton;

    private final GitUsersAdapter adapter = new GitUsersAdapter();

    // а почему у Вас в коде onCreate -> public, а на protected
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        initView();

        adapter.setOnItemClickListener(this::openUserScreen);// ::это ссылка на один метод,
        // а :: означает, что этот метод использовать как лямду чтобы передать его в адаптер
        // и приобразовать его в OnItemClickListener (это синтаксический сахр)

        OnClickLoadButton();
    }

    private void loadData() {
        showProgress(true);
        app.getUsersRepo().getUsers(new GitUsersRepo.Callback() {
            @Override
            public void onSuccess(List<GitUserEntity> users) {
                showProgress(false);
                adapter.setData(users);
            }

            @Override
            public void onError(Throwable throwable) {
                showProgress(false);
                Toast.makeText(UsersActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
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
        loadButton = findViewById(R.id.load_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void OnClickLoadButton() {
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    protected void showProgress(boolean shouldShow) {
        if (shouldShow) {
            recyclerView.setVisibility(View.GONE);//скрываем view со списком
            progressBar.setVisibility(View.VISIBLE);//показываем прогресс загрузки
        } else {
            recyclerView.setVisibility(View.VISIBLE);//показываем view со списком
            progressBar.setVisibility(View.GONE);//скрываем прогресс загрузки
        }
    }
}