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

public class UsersActivity extends BaseGitListActivity<GitUserEntity> {

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

        loadData();
    }

    @Override
    protected Call<List<GitUserEntity>> getRetrofitCall() {
        return getGitHubApi().getUsers();
    }

    @Override
    protected void onSuccess(List<GitUserEntity> data) {
        adapter.setData(data);
    }

    @Override
    protected void onError(Throwable t) {
        Toast.makeText(UsersActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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