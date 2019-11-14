package com.github.acailuv.loanmanager.manage_installment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.github.acailuv.loanmanager.database.CardDao
import com.github.acailuv.loanmanager.database.InstallmentDao
import com.github.acailuv.loanmanager.database.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ChangeInstallmentDetailsFragmentViewModel(
    val userTable: UserDao,
    val cardTable: CardDao,
    val installmentTable: InstallmentDao,
    val currentInstallmentId: Long,
    application: Application
) : AndroidViewModel(application) {

    val job = Job()
    val viewModel = CoroutineScope(Dispatchers.Main + job)



}