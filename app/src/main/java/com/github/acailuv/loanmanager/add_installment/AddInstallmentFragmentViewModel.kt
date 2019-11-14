package com.github.acailuv.loanmanager.add_installment

import android.app.Application
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.acailuv.loanmanager.database.*
import kotlinx.coroutines.*
import java.util.*

class AddInstallmentFragmentViewModel(
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

    private val _cardList = MutableLiveData<List<Card>>()
    val cardList: MutableLiveData<List<Card>>
        get() = _cardList

    init {
        initializeCardList()
    }

    private fun initializeCardList() {
        uiScope.launch {
            _cardList.value = withContext(Dispatchers.IO) {
                println(installmentTable.getInstallments())
                cardTable.getCards()
            }
        }
    }

    var currentCard = MutableLiveData<Card>()

    fun onAddButtonClicked(
        installmentName: EditText,
        tenor: EditText,
        totalLoanAmount: EditText,
        interest: EditText
    ) {

        when {
            installmentName.text.toString() == "" -> _exitStatus.value = "emptyInstallmentName"
            tenor.text.toString() == "" -> _exitStatus.value = "emptyTenor"
            tenor.text.toString().toInt() < 0 -> _exitStatus.value = "invalidTenor"
            totalLoanAmount.text.toString() == "" -> _exitStatus.value = "emptyTotalLoanAmount"
            interest.text.toString() == "" -> _exitStatus.value = "emptyInterest"
            interest.text.toString().toFloat() < 0 -> _exitStatus.value = "invalidInterest"
            else -> {
                val now = Calendar.getInstance()
                val deadline = Calendar.getInstance()
                deadline.add(Calendar.MONTH, tenor.text.toString().toInt())

                val newInstallment = Installment()
                newInstallment.cardId = currentCard.value!!.id
                newInstallment.installmentName = installmentName.text.toString()
                newInstallment.startDate = now.timeInMillis
                newInstallment.endDate = deadline.timeInMillis
                newInstallment.tenorMonth = tenor.text.toString().toInt()
                newInstallment.total = totalLoanAmount.text.toString().toLong()
                newInstallment.interest = interest.text.toString().toFloat() / 100

                uiScope.launch {
                    withContext(Dispatchers.Default) {
                        installmentTable.insert(newInstallment)
                    }
                }

                _exitStatus.value = "OK"
            }
        }
    }

    fun clean() {
        _exitStatus.value = null
        currentCard.value = null
    }

}