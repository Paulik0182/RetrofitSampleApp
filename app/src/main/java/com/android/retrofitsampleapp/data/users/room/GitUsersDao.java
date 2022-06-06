package com.android.retrofitsampleapp.data.users.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.android.retrofitsampleapp.domain.users.GitUserEntity;

import java.util.List;

@Dao
public interface GitUsersDao {

    // расширяем репозиторий.
    @Insert
    void saveUsers(List<GitUserEntity> users);

    // для реализации необходимо передать Callback, через List реализовать мы не можемю.
    // Для реализации необходим асинхронный подход передать Callback
    @Query("SELECT * FROM users")
// users это название таблицы (или можно указать класс сущьности). Мы получаем все из таблиыц users
    List<GitUserEntity> getUsers();

//    @Delete
//    void delete(User user);
}
