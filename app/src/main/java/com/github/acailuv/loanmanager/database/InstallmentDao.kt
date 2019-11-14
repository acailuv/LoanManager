package com.github.acailuv.loanmanager.database

import androidx.room.*

@Dao
interface InstallmentDao {
    @Insert
    fun insert(installment: Installment)

    @Update
    fun update(installment: Installment)

    @Query ("SELECT * FROM installment WHERE id = :id")
    fun getInstallment(id: Long): Installment

    @Query ("SELECT * FROM installment")
    fun getInstallments(): List<Installment>

    @Query ("SELECT * FROM installment ORDER BY end_date")
    fun getInstallmentsByEndDate(): List<Installment>

    @Query ("DELETE FROM installment WHERE id = :id")
    fun delete(id: Long)

    @Query ("UPDATE installment SET paidAmount = :newPaidAmount")
    fun setPaidAmount(newPaidAmount: Long)

    @Query ("UPDATE installment SET status = :newStatus")
    fun setStatus(newStatus: String)

    @Query ("DELETE FROM installment")
    fun clear()
}