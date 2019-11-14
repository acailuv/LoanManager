package com.github.acailuv.loanmanager

import android.app.Application
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.acailuv.loanmanager.database.CardDao
import com.github.acailuv.loanmanager.database.InstallmentDao
import com.github.acailuv.loanmanager.database.UserDao
import kotlinx.coroutines.*

class ChangeUserDataFragmentViewModel(
    val userTable: UserDao,
    val cardTable: CardDao,
    val installmentTable: InstallmentDao,
    application: Application
) : AndroidViewModel(application) {

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val _exitStatus = MutableLiveData<String>()
    val exitStatus: LiveData<String>
        get() = _exitStatus

    fun onApplyClicked(userName: EditText, monthlyIncome: EditText) {
        var userNameString =
            if (userName.text.toString() == "") "User" else userName.text.toString()
        val monthlyIncomeLong =
            if (monthlyIncome.text.toString() == "") 0L else monthlyIncome.text.toString().toLong()

        uiScope.launch {
            val oldUser = withContext(Dispatchers.IO) {
                userTable.getUser(1)
            }
            oldUser.name = userNameString
            oldUser.monthlyIncome = monthlyIncomeLong

            withContext(Dispatchers.Default) {
                userTable.update(oldUser)
            }
        }
        _exitStatus.value = "OK"
    }

    fun clean() {
        _exitStatus.value = null
    }

}