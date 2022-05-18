package com.android.retrofitsampleapp.ui.projects;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.retrofitsampleapp.R;
import com.android.retrofitsampleapp.UsedConst;
import com.android.retrofitsampleapp.domain.Project.GitProjectEntity;
import com.android.retrofitsampleapp.domain.Project.GitProjectRepo;
import com.android.retrofitsampleapp.domain.users.GitUserEntity;
import com.android.retrofitsampleapp.ui.common.BaseActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectsActivity extends BaseActivity {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    private static final String LOGIN_EXTRA_KEY = "LOGIN_EXTRA_KEY";

    private final GitProjectAdapter adapter = new GitProjectAdapter();
    private ImageView avatarImageView;

    public static Intent getLaunchIntent(Context context, String login) {
        Intent intent = new Intent(context, ProjectsActivity.class);
        intent.putExtra(LOGIN_EXTRA_KEY, login);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        initView();

        setTitle(getLogin());//подставили имя в заголовок (не понял как. пояснение.)????

        loadUser(getLogin());
        loadData();
    }

    private void loadData() {
        showProgress(true);
        app.getProjectRepo().getProject(new GitProjectRepo.Callback() {
            @Override
            public void onSuccess(List<GitProjectEntity> projectEntities) {
                showProgress(false);
                adapter.setData(projectEntities);
            }

            @Override
            public void onError(Throwable throwable) {
                showProgress(false);
                Toast.makeText(ProjectsActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadUser(String login) {
        app.getGitHubApi().getUser(login).enqueue(new Callback<GitUserEntity>() {
            @Override
            public void onResponse(@NonNull Call<GitUserEntity> call,
                                   @NonNull Response<GitUserEntity> response) {
                final String avatarUrl = response.body().getAvatarUrl();
                Toast.makeText(ProjectsActivity.this, avatarUrl, Toast.LENGTH_SHORT).show();
                setAvatar(avatarUrl);
            }

            @Override
            public void onFailure(@NonNull Call<GitUserEntity> call, @NonNull Throwable t) {
                Toast.makeText(ProjectsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAvatar(String avatarUrl) {
        Picasso.get()
                .load(avatarUrl)
                .placeholder(UsedConst.imageConst.DEFAULT_IMAGE_CONST)
                .into(avatarImageView);
    }

    private void initView() {
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        avatarImageView = findViewById(R.id.avatar_image_view);
    }

    private String getLogin() {
        return getIntent().getStringExtra(LOGIN_EXTRA_KEY);//получаем логин
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
