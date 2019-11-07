package com.github.acailuv.loanmanager.database

import androidx.room.*

@Dao
interface InstallmentDao {
    @Insert
    fun insert(installment: Installment)

    @Update
    fun update(installment: Installment)

    @Query ("SELECT * FROM installment")
    fun getInstallments(): List<Installment>

    @Query ("DELETE FROM installment WHERE id = :id")
    fun delete(id: Long)

    @Query ("UPDATE installment SET paidAmount = :newPaidAmount")
    fun setPaidAmount(newPaidAmount: Long)

    @Query ("UPDATE installment SET status = :newStatus")
    fun setStatus(newStatus: String)
}