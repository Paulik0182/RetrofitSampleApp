package com.android.retrofitsampleapp.data.Users;

import com.android.retrofitsampleapp.domain.users.GitUserEntity;
import com.android.retrofitsampleapp.domain.users.GitUsersRepo;

import java.util.List;

/**
 * Использование библиотеки snappyDB (сохранение между открытием и закрытия приложения)
 */

public class CachedNetworkUsersRepoImpl implements GitUsersRepo {

    private final GitUsersRepo masterRepo;//первичный репозиторий
    private final GitUsersRepo slaveRepo;//вторичный репозиторий

    public CachedNetworkUsersRepoImpl(GitUsersRepo remoteRepo, GitUsersRepo localRepo) {
        this.masterRepo = remoteRepo;
        this.slaveRepo = localRepo;
    }

    @Override
    public void saveUsers(List<GitUserEntity> users) {
        //pass
    }

    @Override
    public void getUsers(Callback callback) {
        //запрашиваем пользователя remoteRepo
        masterRepo.getUsers(new Callback() {
            @Override
            public void onSuccess(List<GitUserEntity> users) {
                //в случае успеха, сохраняем данные и отправляем
                slaveRepo.saveUsers(users);//кэш данные
                callback.onSuccess(users);//результат
            }

            @Override
            public void onError(Throwable throwable) {
                //в случае не успеха, спрашиваем у другого репозитория
                callback.onError(throwable);//будет сообщение об ошибке,
                // и обращаемся к вторичному репозеторию, если данные есть, будет загружен кэш
                slaveRepo.getUsers(new Callback() {
                    @Override
                    public void onSuccess(List<GitUserEntity> users) {
                        callback.onSuccess(users);
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
