package com.android.retrofitsampleapp.data.project;

import android.content.Context;
import android.widget.Toast;

import com.android.retrofitsampleapp.UsedConst;
import com.android.retrofitsampleapp.domain.project.GitProjectEntity;
import com.android.retrofitsampleapp.domain.project.GitProjectRepo;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.Arrays;
import java.util.List;

/**
 * это полноценный репозиторий который умеет отдавать данные
 */

public class SnappyDbGitProjectRepoImpl implements GitProjectRepo {

    private final Context context;

    public SnappyDbGitProjectRepoImpl(Context context) {
        this.context = context;
    }

    @Override
    public void saveProject(List<GitProjectEntity> gitProjectEntities) {
        try {
            DB snappydb = DBFactory.open(context);//из описания библиотеки snappyDB

            snappydb.put(UsedConst.bdConstKey.PROJECT_BD_KEY, gitProjectEntities.toArray());//положили значения
            //нужно List превратить в массив -> gitProjectEntities.toArray()
        } catch (SnappydbException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getProject(Callback callback) {
        try {
            DB snappydb = DBFactory.open(context);//из описания библиотеки snappyDB

            GitProjectEntity[] arrayProject;
            //дастали значения. !!обязательно класс GitProjectEntity должен поддерживать Serializable
            arrayProject = snappydb.getArray(UsedConst.bdConstKey.PROJECT_BD_KEY, GitProjectEntity.class);
            List<GitProjectEntity> list = Arrays.asList(arrayProject);
            snappydb.close();

            callback.onSuccess(list);

        } catch (SnappydbException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            callback.onError(e);
        }
    }
}
