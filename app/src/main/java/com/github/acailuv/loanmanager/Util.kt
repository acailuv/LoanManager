package com.github.acailuv.loanmanager

import android.content.Context
import android.widget.Toast
import com.github.acailuv.loanmanager.database.Card

fun getNamesFrom(cardList: List<Card>?): MutableList<String> {

    var result: MutableList<String> = mutableListOf()

    cardList?.forEach { card ->
        result.add(card.nickname)
    }

    return result
}

fun showToast(context: Context?, msg: String, lengthCode: Int) {
    Toast.makeText(context, msg, lengthCode).show()
}