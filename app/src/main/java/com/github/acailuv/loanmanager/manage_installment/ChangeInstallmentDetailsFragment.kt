package com.github.acailuv.loanmanager.manage_installment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.github.acailuv.loanmanager.R
import com.github.acailuv.loanmanager.ViewModelFactory
import com.github.acailuv.loanmanager.database.AppDatabase
import com.github.acailuv.loanmanager.databinding.FragmentChangeInstallmentDetailsBinding

class ChangeInstallmentDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentChangeInstallmentDetailsBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_change_installment_details,
            container,
            false
        )

        val application = requireNotNull(this.activity).application
        val userDataSource = AppDatabase.getInstance(application).userDao
        val cardDataSource = AppDatabase.getInstance(application).cardDao
        val installmentDataSource = AppDatabase.getInstance(application).installmentDao
        val arguments = ChangeInstallmentDetailsFragmentArgs.fromBundle(arguments!!)

        val viewModelFactory = ViewModelFactory(
            userDataSource,
            cardDataSource,
            installmentDataSource,
            application,
            currentInstallmentId = arguments.currentInstallmentId
        )
        val viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ChangeInstallmentDetailsFragmentViewModel::class.java)



        binding.viewModel = viewModel

        // Inflate the layout for this fragment
        return binding.root
    }


}
