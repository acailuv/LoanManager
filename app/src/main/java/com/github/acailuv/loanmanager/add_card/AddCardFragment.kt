package com.github.acailuv.loanmanager.add_card


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.github.acailuv.loanmanager.R
import com.github.acailuv.loanmanager.ViewModelFactory
import com.github.acailuv.loanmanager.dashboard.DashboardFragmentViewModel
import com.github.acailuv.loanmanager.database.AppDatabase
import com.github.acailuv.loanmanager.databinding.FragmentAddCardBinding
import com.github.acailuv.loanmanager.showToast

class AddCardFragment : Fragment() {

    lateinit var binding: FragmentAddCardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_card, container, false)

        val application = requireNotNull(this.activity).application
        val userDataSource = AppDatabase.getInstance(application).userDao
        val cardDataSource = AppDatabase.getInstance(application).cardDao
        val installmentDataSource = AppDatabase.getInstance(application).installmentDao

        val viewModelFactory = ViewModelFactory(
            userDataSource,
            cardDataSource,
            installmentDataSource,
            application
        )
        val viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(AddCardFragmentViewModel::class.java)

        val CANNOT_EMPTY = " cannot be empty!"
        viewModel.exitStatus.observe(this, Observer { status ->
            when (status) {
                "emptyNickname" -> showToast(
                    this.context,
                    "Nickname$CANNOT_EMPTY",
                    Toast.LENGTH_SHORT
                )
                "emptyCardNumber" -> showToast(
                    this.context,
                    "Card Number$CANNOT_EMPTY",
                    Toast.LENGTH_SHORT
                )
                "emptyCardholder" -> showToast(
                    this.context,
                    "Name of Card Holder$CANNOT_EMPTY",
                    Toast.LENGTH_SHORT
                )
                "emptyCreditLimit" -> showToast(
                    this.context,
                    "Credit Limit$CANNOT_EMPTY",
                    Toast.LENGTH_SHORT
                )
                "emptyIssuingBank" -> showToast(
                    this.context,
                    "Issuing Bank$CANNOT_EMPTY",
                    Toast.LENGTH_SHORT
                )
                "emptyVariant" -> showToast(
                    this.context,
                    "Card Variant$CANNOT_EMPTY",
                    Toast.LENGTH_SHORT
                )
                "emptyGrade" -> showToast(
                    this.context,
                    "Card Grade$CANNOT_EMPTY",
                    Toast.LENGTH_SHORT
                )
                "OK" -> {
                    this.findNavController().navigate(AddCardFragmentDirections
                        .actionAddCardFragmentToDashboardFragment())
                    viewModel.clean()
                }
            }
        })

        binding.viewModel = viewModel

        // Inflate the layout for this fragment
        return binding.root
    }

}
