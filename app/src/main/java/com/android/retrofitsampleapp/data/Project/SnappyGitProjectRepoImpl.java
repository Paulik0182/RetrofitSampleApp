package com.android.retrofitsampleapp.data.Project;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.android.retrofitsampleapp.domain.Project.GitProjectEntity;
import com.android.retrofitsampleapp.domain.Project.GitProjectRepo;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Использование библиотеки snappyDB (сохранение между открытием и закрытия приложения)
 */

public class SnappyGitProjectRepoImpl extends Application implements GitProjectRepo {

    private final static String PROJECT_BD_KEY = "PROJECT_BD_KEY";

    private final Context context;
    private final GitProjectRepo networkProjectRepo;//для интернета

    public SnappyGitProjectRepoImpl(Context context, GitProjectRepo networkProjectRepo) {
        this.context = context;
        this.networkProjectRepo = networkProjectRepo;
    }

    //когда ктото запрашивает пользователей, он идет в другой такойже репозиторий
    // (репозиторий который может в интернет ходить) и если успех -> запишет кэш (onSuccess),
    // а если не успех, то он вернет результат из памяти (onError)

    @Override
    public void getProject(Callback callback) {
        networkProjectRepo.getProject(new Callback() {
            @Override
            public void onSuccess(List<GitProjectEntity> projectEntities) {
                saveData(projectEntities);
                callback.onSuccess(projectEntities);
            }

            @Override
            public void onError(Throwable throwable) {
                callback.onError(throwable);
                List<GitProjectEntity> projectEntities = getData();
                if (projectEntities != null) {
                    callback.onSuccess(projectEntities);
                }
            }
        });
    }

    private List<GitProjectEntity> getData() {
        try {
            DB snappydb = DBFactory.open(context);//из описания библиотеки snappyDB

            GitProjectEntity[] arrayProject;
            //дастали значения. !!обязательно класс GitUserEntity должен поддерживать Serializable
            arrayProject = snappydb.getArray(PROJECT_BD_KEY, GitProjectEntity.class);
            List<GitProjectEntity> list = Arrays.asList(arrayProject);
            snappydb.close();

            return list;

        } catch (SnappydbException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return new ArrayList<>();
    }

    //загрузка и сохранение
    private void saveData(List<GitProjectEntity> projectEntities) {
        try {
            DB snappydb = DBFactory.open(context);//из описания библиотеки snappyDB

            snappydb.put(PROJECT_BD_KEY, projectEntities.toArray());//положили значения

            //нужно List превратить в массив -> projectEntities.toArray()
        } catch (SnappydbException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
