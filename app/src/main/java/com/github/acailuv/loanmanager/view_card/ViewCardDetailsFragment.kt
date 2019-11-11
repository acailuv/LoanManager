package com.github.acailuv.loanmanager.view_card


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.acailuv.loanmanager.R
import com.github.acailuv.loanmanager.ViewModelFactory
import com.github.acailuv.loanmanager.database.AppDatabase
import com.github.acailuv.loanmanager.databinding.FragmentViewCardDetailsBinding

class ViewCardDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentViewCardDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_view_card_details, container, false)

        val application = requireNotNull(this.activity).application
        val userDataSource = AppDatabase.getInstance(application).userDao
        val cardDataSource = AppDatabase.getInstance(application).cardDao
        val installmentDataSource = AppDatabase.getInstance(application).installmentDao
        val arguments = ViewCardDetailsFragmentArgs.fromBundle(arguments!!)

        val viewModelFactory =
            ViewModelFactory(userDataSource, cardDataSource, installmentDataSource, application, arguments.currentCardId)
        val viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ViewCardDetailsFragmentViewModel::class.java)

        viewModel.currentCard.observe(this, Observer { currentCard ->
            binding.cardNickname.text = currentCard.nickname
            binding.cardDetailsNumber.text = currentCard.cardNumber
            binding.cardholderName.text = currentCard.cardholder
            binding.issuingBank.text = currentCard.bank
            binding.creditLimit.text = currentCard.limit.toString()
            binding.variant.text = currentCard.variant
            binding.grade.text = currentCard.grade
        })

        binding.viewModel = viewModel

        // Inflate the layout for this fragment
        return binding.root
    }


}
