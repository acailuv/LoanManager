package com.github.acailuv.loanmanager.database

import androidx.room.*

@Entity (tableName = "card")
data class Card (
    @PrimaryKey (autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo (name = "nickname")
    var nickname: String = "",

    @ColumnInfo (name = "card_number")
    var cardNumber: String = "",

    @ColumnInfo (name = "cardholder") // Name of cardholder
    var cardholder: String = "",

    @ColumnInfo (name = "limit")
    var limit: Long = 0L,

    @ColumnInfo (name = "bank") // American Express, Standard Chartered, etc.
    var bank: String = "",

    @ColumnInfo (name = "variant") // Travel, Priority, etc.
    var variant: String = "",

    @ColumnInfo (name = "grade") // Gold, Platinum, etc.
    var grade: String = ""
)