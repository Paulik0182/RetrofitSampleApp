package com.android.retrofitsampleapp.data.users;

import com.android.retrofitsampleapp.data.users.room.GitUsersDao;
import com.android.retrofitsampleapp.domain.users.GitUserEntity;
import com.android.retrofitsampleapp.domain.users.GitUsersRepo;

import java.util.List;

public class RoomGitUsersRepoImpl implements GitUsersRepo {

    private final GitUsersDao gitUsersDao;

    public RoomGitUsersRepoImpl(GitUsersDao gitUsersDao) {
        this.gitUsersDao = gitUsersDao;
    }

    @Override
    public void saveUsers(List<GitUserEntity> users) {
        gitUsersDao.saveUsers(users);
    }

    @Override
    public void getUsers(Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<GitUserEntity> users = gitUsersDao.getUsers();
                callback.onSuccess(users);
            }
        }).start();
    }
}
