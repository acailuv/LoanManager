package com.github.acailuv.loanmanager.database

import androidx.room.*

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "monthly_income")
    var monthlyIncome: Long = 0L
)