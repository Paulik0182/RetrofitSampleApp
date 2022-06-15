package com.android.retrofitsampleapp.ui.common;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.retrofitsampleapp.App;

/**
 * это базавая активити, для общей логике нескольких активити.
 * пакет common общий пакет для классов с общей логикой для активити.
 * <p>
 * protected доступность только наследникам конкретно только этому классу
 */

//это общая активити от которой все остальные должны наследоватся
public abstract class BaseActivity extends AppCompatActivity {
    //вариант_1 app
    protected App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = getApp();
    }

    //вариант_2 app
    protected App getApp() {
        return (App) getApplication();
    }
}
