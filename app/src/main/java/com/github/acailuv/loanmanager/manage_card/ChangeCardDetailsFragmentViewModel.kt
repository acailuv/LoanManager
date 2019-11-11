package com.github.acailuv.loanmanager.manage_card

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


/**
 * TODO: Pass data using SafeArgs from recycler view
 */

class ChangeCardDetailsFragmentViewModel(
    val userTable: UserDao,
    val cardTable: CardDao,
    val installmentTable: InstallmentDao,
    private val currentCardId: Long,
    application: Application
) : AndroidViewModel(application) {

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val _exitStatus = MutableLiveData<String>()
    val exitStatus: LiveData<String>
        get() = _exitStatus

    private val _confirmDelete = MutableLiveData<Boolean>()
    val confirmDelete: LiveData<Boolean>
        get() = _confirmDelete

    val currentCard = MutableLiveData<Card>()

    init {
        initializeCurrentCard()
    }

    fun initializeCurrentCard() {
        uiScope.launch {
            currentCard.value = getCardFromId(currentCardId)
        }
    }

    private suspend fun getCardFromId(id: Long): Card {
        return withContext(Dispatchers.IO) {
            var card = cardTable.getCard(id)
            card
        }
    }

    val nickname = currentCard.value?.nickname
    val cardholder = currentCard.value?.cardholder
    val cardNumber = currentCard.value?.cardNumber
    val creditLimit = currentCard.value?.limit
    val issuingBank = currentCard.value?.bank
    val variant = currentCard.value?.variant
    val grade = currentCard.value?.grade

    fun onEditButtonClicked(
        nickname: EditText,
        cardNumber: EditText,
        cardHolder: EditText,
        creditLimit: EditText,
        issuingBank: EditText,
        variant: EditText,
        grade: EditText
    ) {
        val entryNickname: String =
            if (nickname.text.toString() == "") nickname.hint.toString() else nickname.text.toString()
        val entryCardNumber: String =
            if (cardNumber.text.toString() == "") cardNumber.hint.toString() else cardNumber.text.toString()
        val entryCardholder: String =
            if (cardHolder.text.toString() == "") cardHolder.hint.toString() else cardHolder.text.toString()
        val entryBank: String =
            if (issuingBank.text.toString() == "") issuingBank.hint.toString() else issuingBank.text.toString()
        val entryCreditLimit: Long =
            if (creditLimit.text.toString() == "") creditLimit.hint.toString().toLong() else creditLimit.text.toString().toLong()
        val entryVariant: String =
            if (variant.text.toString() == "") variant.hint.toString() else variant.text.toString()
        val entryGrade: String =
            if (grade.text.toString() == "") grade.hint.toString() else grade.text.toString()

        uiScope.launch {
            val newCard = Card(
                id = currentCard.value!!.id,
                nickname = entryNickname,
                cardNumber = entryCardNumber,
                cardholder = entryCardholder,
                limit = entryCreditLimit,
                bank = entryBank,
                variant = entryVariant,
                grade = entryGrade
            )

            withContext(Dispatchers.IO) {
                cardTable.update(newCard)
            }
        }
        _exitStatus.value = "OK"
    }

    fun onDeleteButtonClicked() {
        _confirmDelete.value = true
    }

    fun resetConfirmDelete() {
        _confirmDelete.value = false
    }

    fun deleteCard() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                cardTable.delete(currentCard.value!!.id)
            }
        }
    }

    fun clean() {
        _confirmDelete.value = null
        _exitStatus.value = null
    }

}