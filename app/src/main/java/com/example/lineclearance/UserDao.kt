package com.example.lineclearance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(users: List<User>)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    fun login(username: String, password: String): User?

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCount(): Int
}