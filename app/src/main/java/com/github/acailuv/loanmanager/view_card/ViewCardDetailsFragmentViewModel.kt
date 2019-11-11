package com.github.acailuv.loanmanager.view_card

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.github.acailuv.loanmanager.database.Card
import com.github.acailuv.loanmanager.database.CardDao
import com.github.acailuv.loanmanager.database.InstallmentDao
import com.github.acailuv.loanmanager.database.UserDao
import kotlinx.coroutines.*

class ViewCardDetailsFragmentViewModel(
    val userTable: UserDao,
    val cardTable: CardDao,
    val installmentTable: InstallmentDao,
    val currentCardId: Long,
    application: Application
) : AndroidViewModel(application) {

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    val currentCard = MutableLiveData<Card>()
    init {
        initializeCurrentCard()
    }

    private fun initializeCurrentCard() {
        uiScope.launch {
            currentCard.value = getCardFromId(currentCardId)
        }
    }

    private suspend fun getCardFromId(id: Long): Card {
        return withContext(Dispatchers.IO) {
            val card = cardTable.getCard(id)
            card
        }
    }


}