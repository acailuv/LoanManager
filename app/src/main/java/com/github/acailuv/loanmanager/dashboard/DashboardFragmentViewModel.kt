package com.github.acailuv.loanmanager.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.acailuv.loanmanager.database.*
import com.github.acailuv.loanmanager.getNamesFrom
import kotlinx.coroutines.*

class DashboardFragmentViewModel(
    val userTable: UserDao,
    val cardTable: CardDao,
    val installmentTable: InstallmentDao,
    application: Application
) : AndroidViewModel(application) {

    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    // Live Data
    private val _rawSpinnerContent = MutableLiveData<List<Card>>()
    val rawSpinnerContent: LiveData<List<Card>>
        get() = _rawSpinnerContent

    private val _spinnerContent = MutableLiveData<List<String>>()
    val spinnerContent: LiveData<List<String>>
        get() = _spinnerContent

    var user = MutableLiveData<User>()
    init{
        initializeUserInfo()
    }

    // Initialize User Info
    fun initializeUserInfo() {
        uiScope.launch {
            val userTableContent = withContext(Dispatchers.IO) { userTable.getUsers() }

            if (userTableContent.isEmpty()) {
                withContext(Dispatchers.IO) {
                    userTable.insert(User(1, "User", 0))
                }
            }
            user.value = withContext(Dispatchers.IO) {
                userTable.getUser(1)
            }
        }
    }

    // Spinner Content
    fun initializeSpinnerContent() {
        uiScope.launch {
            _rawSpinnerContent.value = withContext(Dispatchers.IO) {
                cardTable.getAllCards()
            }
            _spinnerContent.value = getNamesFrom(_rawSpinnerContent.value)
        }
    }
}