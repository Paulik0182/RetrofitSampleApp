package com.android.retrofitsampleapp.ui.projects;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.retrofitsampleapp.R;
import com.android.retrofitsampleapp.UsedConst;
import com.android.retrofitsampleapp.domain.GitProjectEntity;
import com.android.retrofitsampleapp.domain.GitUserEntity;
import com.android.retrofitsampleapp.ui.git_common.BaseGitListActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectsActivity extends BaseGitListActivity<GitProjectEntity> {

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

//        Toast.makeText(this, login, Toast.LENGTH_SHORT).show();

        initView();

        setTitle(getLogin());//подставили имя в заголовок (не понял как. пояснение.)????

        setContractViews(progressBar, recyclerView);

        loadUser(getLogin());
        loadData();
    }

    private void loadUser(String login) {
        getGitHubApi().getUser(login).enqueue(new Callback<GitUserEntity>() {
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

    @Override
    protected Call<List<GitProjectEntity>> getRetrofitCall() {
        return getGitHubApi().getProject(getLogin());
    }

    @Override
    protected void onSuccess(List<GitProjectEntity> data) {
        adapter.setData(data);
    }

    @Override
    protected void onError(Throwable t) {
        Toast.makeText(ProjectsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void initView() {
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);

        avatarImageView = findViewById(R.id.avatar_image_view);
    }

    private String getLogin() {
        return getIntent().getStringExtra(LOGIN_EXTRA_KEY);//получаем логин
    }
}
