package com.android.retrofitsampleapp.data.Users;

import android.content.Context;
import android.widget.Toast;

import com.android.retrofitsampleapp.domain.users.GitUserEntity;
import com.android.retrofitsampleapp.domain.users.GitUsersRepo;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Использование библиотеки snappyDB (сохранение между открытием и закрытия приложения)
 */

public class SnappyGitUsersRepoImpl implements GitUsersRepo {

    private final static String USERS_BD_KEY = "USERS_BD_KEY";

    private final GitUsersRepo networkUsersRepo;//для интернета
    private final Context context;//как только появляется какая либо зависимость ее необходимо объявить

    public SnappyGitUsersRepoImpl(Context context, GitUsersRepo networkUsersRepo) {
        this.context = context;
        this.networkUsersRepo = networkUsersRepo;
    }

    //когда ктото запрашивает пользователей, он идет в другой такойже репозиторий
    // (репозиторий который может в интернет ходить) и если успех -> запишет кэш (onSuccess),
    // а если не успех, то он вернет результат из памяти (onError)
    @Override
    public void getUsers(Callback callback) {
        networkUsersRepo.getUsers(new Callback() {
            @Override
            public void onSuccess(List<GitUserEntity> users) {
                saveData(users);
                callback.onSuccess(users);
            }

            @Override
            public void onError(Throwable throwable) {
                callback.onError(throwable);
                List<GitUserEntity> users = getData();
                if (users != null) {
                    callback.onSuccess(users);
                }
            }
        });
    }

    private List<GitUserEntity> getData() {
        try {
            DB snappydb = DBFactory.open(context);//из описания библиотеки snappyDB

            GitUserEntity[] arrayUsers;
            //дастали значения. !!обязательно класс GitUserEntity должен поддерживать Serializable
            arrayUsers = snappydb.getArray(USERS_BD_KEY, GitUserEntity.class);
            List<GitUserEntity> list = Arrays.asList(arrayUsers);
            snappydb.close();

            return list;

        } catch (SnappydbException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return new ArrayList<>();
    }

    //загрузка и сохранение
    private void saveData(List<GitUserEntity> users) {
        try {
            DB snappydb = DBFactory.open(context);//из описания библиотеки snappyDB

            snappydb.put(USERS_BD_KEY, users.toArray());//положили значения

            //нужно List превратить в массив -> users.toArray()
        } catch (SnappydbException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
