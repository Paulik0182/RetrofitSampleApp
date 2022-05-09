package com.android.retrofitsampleapp.ui.git_common;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.retrofitsampleapp.data.GitHubApi;
import com.android.retrofitsampleapp.ui.common.BaseActivity;

public abstract class BaseGitListActivity extends BaseActivity {

    protected ProgressBar progressBar;
    protected RecyclerView recyclerView;

    private GitHubApi gitHubApi;//достаем из класса App из метода GitHubApi -> gitHubApi

    protected GitHubApi getGitHubApi() {
        return gitHubApi;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gitHubApi = app.getGitHubApi();//достаем из класса App из метода GitHubApi -> gitHubApi.
        // при этом арр берется в общем классе BaseActivity, ткак-как от этого класса мы наследуемся
    }

    //не понятно, пояснить
    protected final void setContractViews(
            ProgressBar progressBar,
            RecyclerView recyclerView
    ) {
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
