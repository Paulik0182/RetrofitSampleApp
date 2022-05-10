package com.android.retrofitsampleapp.ui.git_common;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.retrofitsampleapp.UsedConst;
import com.android.retrofitsampleapp.data.GitHubApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseGitListActivity<T> extends UsedConst {

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

    protected final void loadData() {
        showProgress(true);
        getRetrofitCall().enqueue(new Callback<List<T>>() {
            @Override
            public void onResponse(Call<List<T>> call, Response<List<T>> response) {
                showProgress(false);
                if (response.isSuccessful()) { //isSuccessful - это уже проверка кодов от 200 до 300
                    onSuccess(response.body());

                } else {
                    Toast.makeText(BaseGitListActivity.this, "Error code" + response.code(), Toast.LENGTH_SHORT).show();
                    onError(new Throwable("Error code = " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<T>> call, Throwable t) {
                showProgress(false);
                onError(t);
                Toast.makeText(app, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    protected abstract Call<List<T>> getRetrofitCall();

    protected abstract void onSuccess(List<T> data);

    protected abstract void onError(Throwable t);
}
