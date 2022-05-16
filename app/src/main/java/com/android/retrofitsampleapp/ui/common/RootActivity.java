package com.android.retrofitsampleapp.ui.common;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.retrofitsampleapp.App;
import com.android.retrofitsampleapp.ui.projects.ProjectsFragment;
import com.android.retrofitsampleapp.ui.users.UsersFragment;

public class RootActivity extends AppCompatActivity implements UsersFragment.Controller, ProjectsFragment.Controller {
    protected App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApp();
    }

    protected App getApp() {
        return (App) getApplication();
    }

}