package com.android.retrofitsampleapp.data.users;

import com.android.retrofitsampleapp.domain.users.GitUserEntity;
import com.android.retrofitsampleapp.domain.users.GitUsersRepo;

import java.util.List;

/**
 * Временная память!! Кэш память (репозиторий)
 */

public class CachedGitUsersRepoImpl implements GitUsersRepo {

    private final GitUsersRepo networkUsersRepo;//для интернета
    private List<GitUserEntity> cache;

    public CachedGitUsersRepoImpl(GitUsersRepo networkUsersRepo) {
        this.networkUsersRepo = networkUsersRepo;
    }

    @Override
    public void saveUsers(List<GitUserEntity> users) {
        //pass
    }

    //когда ктото запрашивает пользователей, он идет в другой такойже репозиторий
    // (репозиторий который может в интернет ходить) и если успех -> запишет кэш (onSuccess),
    // а если не успех, то он вернет результат из памяти (onError)
    @Override
    public void getUsers(Callback callback) {
        networkUsersRepo.getUsers(new Callback() {
            @Override
            public void onSuccess(List<GitUserEntity> users) {
                cache = users;
                callback.onSuccess(users);
            }
            @Override
            public void onError(Throwable throwable) {
                callback.onError(throwable);
                if (cache != null) {
                    callback.onSuccess(cache);
                }
            }
        });
    }
}
