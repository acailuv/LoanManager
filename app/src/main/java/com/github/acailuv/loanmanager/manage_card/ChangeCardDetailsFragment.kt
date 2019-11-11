package com.github.acailuv.loanmanager.manage_card


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.github.acailuv.loanmanager.R
import com.github.acailuv.loanmanager.ViewModelFactory
import com.github.acailuv.loanmanager.database.AppDatabase
import com.github.acailuv.loanmanager.databinding.FragmentChangeCardDetailsBinding
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.acailuv.loanmanager.showToast


class ChangeCardDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentChangeCardDetailsBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_change_card_details,
            container,
            false
        )

        val application = requireNotNull(this.activity).application
        val userDataSource = AppDatabase.getInstance(application).userDao
        val cardDataSource = AppDatabase.getInstance(application).cardDao
        val installmentDataSource = AppDatabase.getInstance(application).installmentDao
        val arguments = ChangeCardDetailsFragmentArgs.fromBundle(arguments!!)

        val viewModelFactory =
            ViewModelFactory(
                userDataSource,
                cardDataSource,
                installmentDataSource,
                application,
                arguments.currentCardId
            )
        val viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ChangeCardDetailsFragmentViewModel::class.java)

        viewModel.currentCard.observe(this, Observer { currentCard ->
            binding.cardNickname.hint = currentCard.nickname
            binding.cardNumber.hint = currentCard.cardNumber
            binding.cardholderName.hint = currentCard.cardholder
            binding.issuingBank.hint = currentCard.bank
            binding.creditLimit.hint = currentCard.limit.toString()
            binding.variant.hint = currentCard.variant
            binding.grade.hint = currentCard.grade
        })

        viewModel.exitStatus.observe(this, Observer { status ->
            when (status) {
                "OK" -> {
                    this.findNavController().navigate(
                        ChangeCardDetailsFragmentDirections
                            .actionChangeCardDetailsFragmentToManageCardFragment()
                    )
                    viewModel.clean()
                }
            }
        })

        viewModel.confirmDelete.observe(this, Observer {
            if (it == true) {
                AlertDialog.Builder(this.context!!)
                    .setTitle("Delete This Card?")
                    .setMessage("Are you sure you want to delete this card? This cannot be undone.")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes) { _, _ ->
                        viewModel.deleteCard()
                        this.findNavController().navigate(
                            ChangeCardDetailsFragmentDirections
                                .actionChangeCardDetailsFragmentToManageCardFragment()
                        )
                        viewModel.clean()
                    }

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
                viewModel.resetConfirmDelete()
            }
        })

        binding.viewModel = viewModel

        // Inflate the layout for this fragment
        return binding.root
    }


}
