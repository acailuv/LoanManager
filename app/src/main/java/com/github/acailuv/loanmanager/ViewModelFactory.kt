package com.github.acailuv.loanmanager

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.acailuv.loanmanager.add_card.AddCardFragmentViewModel
import com.github.acailuv.loanmanager.add_installment.AddInstallmentFragmentViewModel
import com.github.acailuv.loanmanager.dashboard.DashboardFragmentViewModel
import com.github.acailuv.loanmanager.database.Card
import com.github.acailuv.loanmanager.database.CardDao
import com.github.acailuv.loanmanager.database.InstallmentDao
import com.github.acailuv.loanmanager.database.UserDao
import com.github.acailuv.loanmanager.manage_card.ChangeCardDetailsFragmentViewModel
import com.github.acailuv.loanmanager.manage_card.ManageCardFragmentViewModel
import com.github.acailuv.loanmanager.view_card.ViewCardDetailsFragmentViewModel
import com.github.acailuv.loanmanager.view_card.ViewCardFragmentViewModel
import com.github.acailuv.loanmanager.view_installment.ViewInstallmentDetailsFragmentViewModel
import com.github.acailuv.loanmanager.view_installment.ViewInstallmentFragmentViewModel

class ViewModelFactory(
    private val userDataSource: UserDao,
    private val cardDataSource: CardDao,
    private val installmentDataSource: InstallmentDao,
    private val application: Application,
    private val currentCardId: Long = 0L,
    private val currentInstallmentId: Long = 0L
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(ManageCardFragmentViewModel::class.java) -> return ManageCardFragmentViewModel(
                userDataSource,
                cardDataSource,
                installmentDataSource,
                application
            ) as T
            modelClass.isAssignableFrom(DashboardFragmentViewModel::class.java) -> return DashboardFragmentViewModel(
                userDataSource,
                cardDataSource,
                installmentDataSource,
                application
            ) as T
            modelClass.isAssignableFrom(AddCardFragmentViewModel::class.java) -> return AddCardFragmentViewModel(
                userDataSource,
                cardDataSource,
                installmentDataSource,
                application
            ) as T
            modelClass.isAssignableFrom(ChangeCardDetailsFragmentViewModel::class.java) -> return ChangeCardDetailsFragmentViewModel(
                userDataSource,
                cardDataSource,
                installmentDataSource,
                currentCardId,
                application
            ) as T
            modelClass.isAssignableFrom(ViewCardFragmentViewModel::class.java) -> return ViewCardFragmentViewModel(
                userDataSource,
                cardDataSource,
                installmentDataSource,
                application
            ) as T
            modelClass.isAssignableFrom(ViewCardDetailsFragmentViewModel::class.java) -> return ViewCardDetailsFragmentViewModel(
                userDataSource,
                cardDataSource,
                installmentDataSource,
                currentCardId,
                application
            ) as T
            modelClass.isAssignableFrom(AddInstallmentFragmentViewModel::class.java) -> return AddInstallmentFragmentViewModel(
                userDataSource,
                cardDataSource,
                installmentDataSource,
                application
            ) as T
            modelClass.isAssignableFrom(ViewInstallmentFragmentViewModel::class.java) -> return ViewInstallmentFragmentViewModel(
                userDataSource,
                cardDataSource,
                installmentDataSource,
                application
            ) as T
            modelClass.isAssignableFrom(ViewInstallmentDetailsFragmentViewModel::class.java) -> return ViewInstallmentDetailsFragmentViewModel(
                userDataSource,
                cardDataSource,
                installmentDataSource,
                currentInstallmentId,
                application
            ) as T
            else -> throw IllegalAccessException("ViewModel class not recognized.")
        }
    }

}