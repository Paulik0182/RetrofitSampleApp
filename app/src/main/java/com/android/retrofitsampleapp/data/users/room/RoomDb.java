package com.android.retrofitsampleapp.data.users.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.android.retrofitsampleapp.domain.users.GitUserEntity;

@Database(entities = {GitUserEntity.class}, version = 1)
public abstract class RoomDb extends RoomDatabase {
    public abstract GitUsersDao gitUsersDao();
}
