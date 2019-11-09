package com.github.acailuv.loanmanager.add_card

import android.app.Application
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.acailuv.loanmanager.database.Card
import com.github.acailuv.loanmanager.database.CardDao
import com.github.acailuv.loanmanager.database.InstallmentDao
import com.github.acailuv.loanmanager.database.UserDao
import kotlinx.coroutines.*
import java.lang.Exception

class AddCardFragmentViewModel(
    val userTable: UserDao,
    val cardTable: CardDao,
    val installmentTable: InstallmentDao,
    application: Application
) : AndroidViewModel(application) {

    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val _exitStatus = MutableLiveData<String>()
    val exitStatus: LiveData<String>
        get() = _exitStatus

    fun onAddButtonClicked(
        nickname: EditText,
        cardNumber: EditText,
        cardHolder: EditText,
        creditLimit: EditText,
        issuingBank: EditText,
        variant: EditText,
        grade: EditText
    ) {
        when {
            nickname.text.toString() == "" -> _exitStatus.value = "emptyNickname"
            cardNumber.text.toString() == "" -> _exitStatus.value = "emptyCardNumber"
            cardHolder.text.toString() == "" -> _exitStatus.value = "emptyCardholder"
            creditLimit.text.toString() == "" -> _exitStatus.value = "emptyCreditLimit"
            issuingBank.text.toString() == "" -> _exitStatus.value = "emptyIssuingBank"
            variant.text.toString() == "" -> _exitStatus.value = "emptyVariant"
            grade.text.toString() == "" -> _exitStatus.value = "emptyGrade"
            else -> {
                uiScope.launch {
                    val newCard = Card(
                        nickname = nickname.text.toString(),
                        cardNumber = cardNumber.text.toString(),
                        cardholder = cardHolder.text.toString(),
                        limit = creditLimit.text.toString().toLong(),
                        bank = issuingBank.text.toString(),
                        variant = variant.text.toString(),
                        grade = grade.text.toString()
                    )

                    withContext(Dispatchers.IO) {
                        cardTable.insert(newCard)
                    }
                }
                _exitStatus.value = "OK"
            }
        }
    }

    fun clean() {
        _exitStatus.value = null
    }
}