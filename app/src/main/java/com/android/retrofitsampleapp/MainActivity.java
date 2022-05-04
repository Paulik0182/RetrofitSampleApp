package com.android.retrofitsampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .build();

    private GitHubApi gitHubApi = retrofit.create(GitHubApi.class); //создаем gitHubApi. Автоматически обратится к интерфейсу

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //многопоточность. Запуск паралельного потока
//        new Thread(){
//          //todo
//        }.start();

        gitHubApi.getProject("borhammere").enqueue(new Callback<List<GitProjectEntity>>() {
           //получение ответа
            @Override
            public void onResponse(Call<List<GitProjectEntity>> call, Response<List<GitProjectEntity>> response) {
//                if (response.code()==200){  //проверка кода, код 200 означает успех
                if (response.isSuccessful()){ //isSuccessful - это уже проверка кодов от 200 до 300
                    List<GitProjectEntity> projects = response.body(); // body - это тело запроса, это будет список репозиториев которые мы ищем. Здесь мы получаем список проектов
                } else {
                    Toast.makeText(MainActivity.this, "Error code" +response.code(), Toast.LENGTH_LONG).show();
                }
            }

            //получение ошибки. чтото сломалось (нет сети и т.д.)
            @Override
            public void onFailure(Call<List<GitProjectEntity>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}