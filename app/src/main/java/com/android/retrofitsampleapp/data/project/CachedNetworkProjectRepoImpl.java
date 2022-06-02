package com.android.retrofitsampleapp.data.project;

import android.app.Application;

import com.android.retrofitsampleapp.domain.project.GitProjectEntity;
import com.android.retrofitsampleapp.domain.project.GitProjectRepo;

import java.util.List;

/**
 * Использование библиотеки snappyDB (сохранение между открытием и закрытия приложения)
 */

public class CachedNetworkProjectRepoImpl extends Application implements GitProjectRepo {

    private final GitProjectRepo masterRepo;//первичный репозиторий
    private final GitProjectRepo slaveRepo;//вторичный репозиторий

    public CachedNetworkProjectRepoImpl(GitProjectRepo masterRepo, GitProjectRepo slaveRepo) {
        this.masterRepo = masterRepo;
        this.slaveRepo = slaveRepo;
    }

    //когда ктото запрашивает пользователей, он идет в другой такойже репозиторий
    // (репозиторий который может в интернет ходить) и если успех -> запишет кэш (onSuccess),
    // а если не успех, то он вернет результат из памяти (onError)


    @Override
    public void saveProject(List<GitProjectEntity> projectEntities) {

    }

    @Override
    public void getProject(Callback callback) {
        //запрашиваем пользователя remoteRepo
        masterRepo.getProject(new Callback() {
            @Override
            public void onSuccess(List<GitProjectEntity> projectEntities) {
                //в случае успеха, сохраняем данные и отправляем
                slaveRepo.saveProject(projectEntities);//кэш данные
                callback.onSuccess(projectEntities);//результат
            }

            @Override
            public void onError(Throwable throwable) {
                //в случае не успеха, спрашиваем у другого репозитория
                callback.onError(throwable);//будет сообщение об ошибке,
                // и обращаемся к вторичному репозеторию, если данные есть, будет загружен кэш
                slaveRepo.getProject(new Callback() {
                    @Override
                    public void onSuccess(List<GitProjectEntity> projectEntities) {
                        callback.onSuccess(projectEntities);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        callback.onError(throwable);
                    }
                });
            }
        });
    }
}
