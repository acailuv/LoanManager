package com.github.acailuv.loanmanager.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CardDao {
    @Insert
    fun insert(card: Card)

    @Update
    fun update(card: Card)

    @Query ("SELECT * FROM card")
    fun getCards(): List<Card>

    @Query ("DELETE FROM card WHERE id = :id")
    fun delete(id: Long)

    @Query ("DELETE FROM card")
    fun clear()
}