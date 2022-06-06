package com.android.retrofitsampleapp.data.users;

import android.os.Handler;

import com.android.retrofitsampleapp.data.users.room.GitUsersDao;
import com.android.retrofitsampleapp.domain.users.GitUserEntity;
import com.android.retrofitsampleapp.domain.users.GitUsersRepo;

import java.util.List;

public class RoomGitUsersRepoImpl implements GitUsersRepo {

    private final GitUsersDao gitUsersDao;
    private final Handler uiHandler;

    public RoomGitUsersRepoImpl(GitUsersDao gitUsersDao, Handler uiHandler) {
        this.gitUsersDao = gitUsersDao;
        this.uiHandler = uiHandler;
    }

    @Override
    public void saveUsers(List<GitUserEntity> users) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                gitUsersDao.saveUsers(users);
            }
        }).start();

    }

    @Override
    public void getUsers(Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<GitUserEntity> users = gitUsersDao.getUsers();
                uiHandler.post(new Runnable() {//перенаправили на главный поток
                    @Override
                    public void run() {
                        callback.onSuccess(users);
                    }
                });
            }
        }).start();
    }
}
