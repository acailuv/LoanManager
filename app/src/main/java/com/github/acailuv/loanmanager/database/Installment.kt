package com.github.acailuv.loanmanager.database

import androidx.room.*
import java.util.*

@Entity (tableName = "installment")
data class Installment (
    @PrimaryKey (autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo (name = "installment_name")
    var installmentName: String = "",

    @ColumnInfo (name = "card_id")
    var cardId: Long = 0L,

    @ColumnInfo (name = "start_date")
    var startDate: Long = 0L, // More info here: https://developer.android.com/reference/java/util/Date

    @ColumnInfo (name = "end_date")
    var endDate: Long = 1L,

    @ColumnInfo (name = "total")
    var total: Long = 0L,

    @ColumnInfo (name = "interest") // Monthly interest percentage (*12 to convert to annual interest)
    var interest: Float = 0.0F,

    @ColumnInfo (name = "paidAmount")
    var paidAmount: Long = 0L,

    @ColumnInfo (name = "status")
    var status: String = "NOT PAID"
)