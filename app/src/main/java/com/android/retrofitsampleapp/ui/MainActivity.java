package com.android.retrofitsampleapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.retrofitsampleapp.R;
import com.android.retrofitsampleapp.data.GitHubApi;
import com.android.retrofitsampleapp.domain.GitProjectEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())// это приобразователь объектов из одного типа в другой тип (здесь старонняя библиотека)
            .build();

    private final GitHubApi gitHubApi = retrofit.create(GitHubApi.class); //создаем gitHubApi. Автоматически обратится к интерфейсу

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private final GitProjectAdapter adapter = new GitProjectAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        loadProjects("borhammere");
    }

    private void loadProjects(String username) {
        showProgress(true);
        gitHubApi.getProject(username).enqueue(new Callback<List<GitProjectEntity>>() {
            //получение ответа
            @Override
            public void onResponse(Call<List<GitProjectEntity>> call, Response<List<GitProjectEntity>> response) {
                showProgress(false);
//                if (response.code()==200){  //проверка кода, код 200 означает успех
                if (response.isSuccessful()) { //isSuccessful - это уже проверка кодов от 200 до 300
                    List<GitProjectEntity> projects = response.body(); // body - это тело запроса, это будет список репозиториев которые мы ищем. Здесь мы получаем список проектов

                    adapter.setData(projects);
                    //test
                    Toast.makeText(MainActivity.this, "Size" + projects.size(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error code" + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            //получение ошибки. чтото сломалось (нет сети и т.д.)
            @Override
            public void onFailure(Call<List<GitProjectEntity>> call, Throwable t) {
                showProgress(false);
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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