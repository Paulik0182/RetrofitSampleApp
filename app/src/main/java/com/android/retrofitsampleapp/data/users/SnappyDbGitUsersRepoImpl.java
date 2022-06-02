package com.android.retrofitsampleapp.data.users;

import android.content.Context;
import android.widget.Toast;

import com.android.retrofitsampleapp.UsedConst;
import com.android.retrofitsampleapp.domain.users.GitUserEntity;
import com.android.retrofitsampleapp.domain.users.GitUsersRepo;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.Arrays;
import java.util.List;

/**
 * это полноценный репозиторий который умеет отдавать данные
 */

public class SnappyDbGitUsersRepoImpl implements GitUsersRepo {

    private final Context context;

    public SnappyDbGitUsersRepoImpl(Context context) {
        this.context = context;
    }

    @Override
    public void saveUsers(List<GitUserEntity> users) {
        try {
            DB snappydb = DBFactory.open(context);//из описания библиотеки snappyDB

            snappydb.put(UsedConst.bdConstKey.USERS_BD_KEY, users.toArray());//положили значения

            //нужно List превратить в массив -> users.toArray()
        } catch (SnappydbException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getUsers(Callback callback) {
        try {
            DB snappydb = DBFactory.open(context);//из описания библиотеки snappyDB

            GitUserEntity[] arrayUsers;
            //дастали значения. !!обязательно класс GitUserEntity должен поддерживать Serializable
            arrayUsers = snappydb.getArray(UsedConst.bdConstKey.USERS_BD_KEY, GitUserEntity.class);
            List<GitUserEntity> list = Arrays.asList(arrayUsers);
            snappydb.close();

            callback.onSuccess(list);

        } catch (SnappydbException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            callback.onError(e);
        }
    }
}
