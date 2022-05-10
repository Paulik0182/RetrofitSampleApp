package com.android.retrofitsampleapp.ui.projects;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.retrofitsampleapp.R;
import com.android.retrofitsampleapp.domain.GitProjectEntity;
import com.android.retrofitsampleapp.ui.git_common.BaseGitListActivity;

import java.util.List;

import retrofit2.Call;

public class ProjectsActivity extends BaseGitListActivity<GitProjectEntity> {

    private final GitProjectAdapter adapter = new GitProjectAdapter();

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

        loadData();

        //второй вариант написания
//        adapter.setOnItemClickListener(user ->{
//            openUserScreen(user);
//        });

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
    }

    private String getLogin() {
        return getIntent().getStringExtra(LOGIN_EXTRA_KEY);//получаем логин
    }
}
