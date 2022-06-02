package com.android.retrofitsampleapp.data.project;

import android.app.Application;

import com.android.retrofitsampleapp.domain.project.GitProjectEntity;
import com.android.retrofitsampleapp.domain.project.GitProjectRepo;

import java.util.List;

/**
 * Временная память!! Кэш память (репозиторий)
 */

public class CachedGitProjectRepoImpl extends Application implements GitProjectRepo {

    private final GitProjectRepo networkProjectRepo;//для интернета
    private List<GitProjectEntity> cache;

    public CachedGitProjectRepoImpl(GitProjectRepo networkProjectRepo) {
        this.networkProjectRepo = networkProjectRepo;
    }

    //когда ктото запрашивает пользователей, он идет в другой такойже репозиторий
    // (репозиторий который может в интернет ходить) и если успех -> запишет кэш (onSuccess),
    // а если не успех, то он вернет результат из памяти (onError)

    @Override
    public void saveProject(List<GitProjectEntity> projectEntities) {
        //pass
    }

    @Override
    public void getProject(Callback callback) {
        networkProjectRepo.getProject(new Callback() {
            @Override
            public void onSuccess(List<GitProjectEntity> projectEntities) {
                cache = projectEntities;
                callback.onSuccess(projectEntities);
            }

            @Override
            public void onError(Throwable throwable) {
                callback.onError(throwable);
//                Toast.makeText(context, "ПРИШЕЛ NULL", Toast.LENGTH_SHORT).show();
                if (cache != null) {
                    callback.onSuccess(cache);
                }
            }
        });
    }
}
