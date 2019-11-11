package com.github.acailuv.loanmanager.database

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Query ("SELECT * FROM user")
    fun getUsers(): List<User>

    @Query ("SELECT * FROM user WHERE id = :id")
    fun getUser(id: Long): User

    @Query ("DELETE FROM user WHERE id = :id")
    fun delete(id: Long)

    @Query ("DELETE FROM user")
    fun clear()
}