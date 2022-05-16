package com.android.retrofitsampleapp.ui.git_common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.retrofitsampleapp.data.GitHubApi;
import com.android.retrofitsampleapp.ui.common.BaseFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseGitListFragmenty<T> extends BaseFragment {

    protected ProgressBar progressBar;
    protected RecyclerView recyclerView;

    private GitHubApi gitHubApi;//достаем из класса App из метода GitHubApi -> gitHubApi

    protected GitHubApi getGitHubApi() {
        return gitHubApi;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gitHubApi = app.getGitHubApi();//достаем из класса App из метода GitHubApi -> gitHubApi.
        // при этом арр берется в общем классе BaseFragment, ткак-как от этого класса мы наследуемся
    }

    //не понятно, пояснить
    protected final void setContractViews(
            ProgressBar progressBar,
            RecyclerView recyclerView
    ) {
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
            public void onResponse(@NonNull Call<List<T>> call, @NonNull Response<List<T>> response) {
                showProgress(false);
                if (response.isSuccessful()) { //isSuccessful - это уже проверка кодов от 200 до 300
                    onSuccess(response.body());

                } else {
                    Toast.makeText(getContext(), "Error code" + response.code(), Toast.LENGTH_SHORT).show();
                    onError(new Throwable("Error code = " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<T>> call, @NonNull Throwable t) {
                showProgress(false);
                onError(t);
                Toast.makeText(app, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    protected Call<List<T>> getRetrofitCall() {
        return null;
    }

    protected void onSuccess(List<T> data) {

    }

    protected void onError(Throwable t) {

    }
}
