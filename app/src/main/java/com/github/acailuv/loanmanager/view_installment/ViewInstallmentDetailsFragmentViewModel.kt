package com.github.acailuv.loanmanager.view_installment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.acailuv.loanmanager.database.*
import kotlinx.coroutines.*

class ViewInstallmentDetailsFragmentViewModel(
    val userTable: UserDao,
    val cardTable: CardDao,
    val installmentTable: InstallmentDao,
    val currentInstallmentId: Long,
    application: Application
) : AndroidViewModel(application) {

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val _currentInstallment = MutableLiveData<Installment>()
    val currentInstallment: LiveData<Installment>
    get() = _currentInstallment
    init {
        initializeInstallment()
    }
    private fun initializeInstallment() {
        uiScope.launch {
            _currentInstallment.value = withContext(Dispatchers.IO) {
                installmentTable.getInstallment(currentInstallmentId)
            }
        }
    }

    private val _currentInstallmentCard = MutableLiveData<Card>()
    val currentInstallmentCard: LiveData<Card>
    get() = _currentInstallmentCard
    init {
        initializeInstallmentCard()
    }
    private fun initializeInstallmentCard() {
        uiScope.launch {
            _currentInstallmentCard.value = withContext(Dispatchers.IO) {
                while (_currentInstallment.value == null);
                cardTable.getCard(_currentInstallment.value!!.cardId)
            }
        }
    }

    private val _exitStatus = MutableLiveData<String>()
    val exitStatus: LiveData<String>
    get() = _exitStatus

    private val _confirmDelete = MutableLiveData<Boolean>()
    val confirmDelete: LiveData<Boolean>
    get() = _confirmDelete

    fun onDeleteButtonClicked() {
        _confirmDelete.value = true
    }

    fun resetConfirmDelete() {
        _confirmDelete.value = false
    }

    fun deleteInstallment() {
        uiScope.launch {
            withContext(Dispatchers.Default) {
                installmentTable.delete(currentInstallmentId)
            }
            _exitStatus.value = "OK"
        }
    }

    fun clean() {
        _exitStatus.value = null
    }

}