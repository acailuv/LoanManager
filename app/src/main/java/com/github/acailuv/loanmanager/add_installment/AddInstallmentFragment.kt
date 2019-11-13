package com.github.acailuv.loanmanager.add_installment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.github.acailuv.loanmanager.R
import com.github.acailuv.loanmanager.ViewModelFactory
import com.github.acailuv.loanmanager.database.AppDatabase
import com.github.acailuv.loanmanager.databinding.FragmentAddInstallmentBinding
import com.github.acailuv.loanmanager.showToast

class AddInstallmentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAddInstallmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_installment, container, false)

        val application = requireNotNull(this.activity).application
        val userDataSource = AppDatabase.getInstance(application).userDao
        val cardDataSource = AppDatabase.getInstance(application).cardDao
        val installmentDataSource = AppDatabase.getInstance(application).installmentDao

        val viewModelFactory =
            ViewModelFactory(userDataSource, cardDataSource, installmentDataSource, application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(AddInstallmentFragmentViewModel::class.java)

        val CANNOT_EMPTY = " cannot be empty!"
        viewModel.exitStatus.observe(this, Observer { status ->
            when (status) {
                "emptyInstallmentName" -> showToast(
                    this.context,
                    "Installment Name$CANNOT_EMPTY",
                    Toast.LENGTH_SHORT
                )
                "emptyTenor" -> showToast(this.context, "Tenor$CANNOT_EMPTY", Toast.LENGTH_SHORT)
                "emptyTotalLoanAmount" -> showToast(
                    this.context,
                    "Total Loan Amount$CANNOT_EMPTY",
                    Toast.LENGTH_SHORT
                )
                "emptyInterest" -> showToast(
                    this.context,
                    "Interest$CANNOT_EMPTY",
                    Toast.LENGTH_SHORT
                )
                "invalidInterest" -> showToast(
                    this.context,
                    "Interest should not be negative.",
                    Toast.LENGTH_SHORT
                )
                "invalidTenor" -> showToast(
                    this.context,
                    "Tenor should not be negative.",
                    Toast.LENGTH_SHORT
                )
                "OK" -> {
                    this.findNavController()
                        .navigate(AddInstallmentFragmentDirections.actionAddInstallmentFragmentToDashboardFragment())
                    viewModel.clean()
                }
            }
        })

        viewModel.cardList.observe(this, Observer {
            if (it.isEmpty()) {
                binding.cardSelectSpinner.adapter = null
            } else {
                var cardNames: MutableList<String> = mutableListOf()
                for (i in 0 until it.size) {
                    cardNames.add(it[i].nickname)
                }

                val arrayAdapter =
                    ArrayAdapter(this.context!!, android.R.layout.simple_spinner_item, cardNames)
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.cardSelectSpinner.adapter = arrayAdapter
                binding.cardSelectSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            viewModel.currentCard.value = viewModel.cardList.value?.get(position)
                            binding.cardDetailsNumber.text =
                                viewModel.currentCard.value?.cardNumber
                            binding.cardholderName.text =
                                viewModel.currentCard.value?.cardholder
                            binding.creditLimit.text =
                                viewModel.currentCard.value?.limit.toString()
                            binding.issuingBank.text =
                                viewModel.currentCard.value?.bank
                            binding.variant.text =
                                viewModel.currentCard.value?.variant
                            binding.grade.text =
                                viewModel.currentCard.value?.grade
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // Do absolutely nothing
                        }
                    }

            }
        })

        binding.viewModel = viewModel

        // Inflate the layout for this fragment
        return binding.root
    }


}
