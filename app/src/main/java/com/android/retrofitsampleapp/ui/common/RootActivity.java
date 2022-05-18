package com.android.retrofitsampleapp.ui.common;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.retrofitsampleapp.App;
import com.android.retrofitsampleapp.R;
import com.android.retrofitsampleapp.domain.GitProjectEntity;
import com.android.retrofitsampleapp.domain.GitUserEntity;
import com.android.retrofitsampleapp.ui.projects.ProjectsFragment;
import com.android.retrofitsampleapp.ui.users.UsersFragment;

public class RootActivity extends AppCompatActivity implements UsersFragment.Controller, ProjectsFragment.Controller {

    private static final String TAG_USER_CONTAINER_LAYOUT_KEY = "TAG_MAIN_CONTAINER_LAYOUT_KEY";
    private static final String TAG_PROJECT_CONTAINER_LAYOUT_KEY = "TAG_DETAIL_CONTAINER_LAYOUT_KEY";

    protected App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        getApp();

        Fragment usersFragment = new UsersFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_layout, usersFragment, TAG_USER_CONTAINER_LAYOUT_KEY)
                .commit();
    }

    protected App getApp() {
        return (App) getApplication();
    }

    @Override
    public void showProjectScreen(GitProjectEntity gitProjectEntity) {
        Fragment projectFragment = ProjectsFragment.newInstance(gitProjectEntity);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_layout, projectFragment, TAG_PROJECT_CONTAINER_LAYOUT_KEY)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showUserScreen(GitUserEntity gitUserEntity) {
//        Fragment usersFragment = new UsersFragment.newInstance(gitUserEntity);
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.container_layout, usersFragment, TAG_PROJECT_CONTAINER_LAYOUT_KEY)
//                .addToBackStack(null)
//                .commit();

    }
}