package com.github.acailuv.loanmanager.add_card

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.acailuv.loanmanager.database.CardDao
import com.github.acailuv.loanmanager.database.InstallmentDao
import com.github.acailuv.loanmanager.database.UserDao

class AddCardFragmentViewModelFactory(
    private val userDataSource: UserDao,
    private val cardDataSource: CardDao,
    private val installmentDataSource: InstallmentDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddCardFragmentViewModel::class.java)) {
            return AddCardFragmentViewModel(
                userDataSource,
                cardDataSource,
                installmentDataSource,
                application
            ) as T
        }
        throw IllegalAccessException("ViewModel class not recognized.")
    }

}