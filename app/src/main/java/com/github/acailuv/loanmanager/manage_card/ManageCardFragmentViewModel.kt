package com.github.acailuv.loanmanager.manage_card

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.acailuv.loanmanager.database.Card
import com.github.acailuv.loanmanager.database.CardDao
import com.github.acailuv.loanmanager.database.InstallmentDao
import com.github.acailuv.loanmanager.database.UserDao
import kotlinx.coroutines.*

class ManageCardFragmentViewModel(
    val userTable: UserDao,
    val cardTable: CardDao,
    val installmentTable: InstallmentDao,
    application: Application
) : AndroidViewModel(application) {

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val _cardList = MutableLiveData<List<Card>>()
    val cardList: LiveData<List<Card>>
        get() = _cardList

    fun initializeCardList() {
        uiScope.launch {
            _cardList.value = withContext(Dispatchers.IO) {
                cardTable.getAllCards()
            }
        }
    }
}