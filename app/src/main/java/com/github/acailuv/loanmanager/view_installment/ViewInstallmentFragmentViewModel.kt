package com.github.acailuv.loanmanager.view_installment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.acailuv.loanmanager.database.CardDao
import com.github.acailuv.loanmanager.database.Installment
import com.github.acailuv.loanmanager.database.InstallmentDao
import com.github.acailuv.loanmanager.database.UserDao
import kotlinx.coroutines.*

class ViewInstallmentFragmentViewModel(
    val userTable: UserDao,
    val cardTable: CardDao,
    val installmentTable: InstallmentDao,
    application: Application
) : AndroidViewModel(application) {

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val _installmentList = MutableLiveData<List<Installment>>()
    val installmentList: LiveData<List<Installment>>
        get() = _installmentList
        init {
            initializeInstallmentList()
        }
    private fun initializeInstallmentList() {
        uiScope.launch {
            _installmentList.value = withContext(Dispatchers.IO) {
                installmentTable.getInstallments()
            }
        }
    }

}