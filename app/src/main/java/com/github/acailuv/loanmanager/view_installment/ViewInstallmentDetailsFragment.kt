package com.github.acailuv.loanmanager.view_installment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.github.acailuv.loanmanager.R
import com.github.acailuv.loanmanager.ViewModelFactory
import com.github.acailuv.loanmanager.database.AppDatabase
import com.github.acailuv.loanmanager.databinding.FragmentViewInstallmentDetailsBinding
import java.text.SimpleDateFormat
import java.util.*


class ViewInstallmentDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentViewInstallmentDetailsBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_view_installment_details,
            container,
            false
        )

        val application = requireNotNull(this.activity).application
        val userDataSource = AppDatabase.getInstance(application).userDao
        val cardDataSource = AppDatabase.getInstance(application).cardDao
        val installmentDataSource = AppDatabase.getInstance(application).installmentDao
        val arguments = ViewInstallmentDetailsFragmentArgs.fromBundle(arguments!!)

        val viewModelFactory = ViewModelFactory(
            userDataSource,
            cardDataSource,
            installmentDataSource,
            application,
            currentInstallmentId = arguments.currentInstallmentId
        )
        val viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ViewInstallmentDetailsFragmentViewModel::class.java)

        viewModel.currentInstallment.observe(this, Observer { currentInstallment ->
            val dateFormatter: SimpleDateFormat = SimpleDateFormat("dd MMMM yyyy")
            val startDate = Calendar.getInstance()
            startDate.timeInMillis = currentInstallment.startDate
            val endDate = Calendar.getInstance()
            endDate.timeInMillis = currentInstallment.endDate

            binding.installmentName.text = currentInstallment.installmentName
            binding.startDate.text = dateFormatter.format(startDate.time)
            binding.endDate.text = dateFormatter.format(endDate.time)
            binding.tenorMonth.text = currentInstallment.tenorMonth.toString() + " Month(s)"
            binding.totalLoanAmount.text = currentInstallment.total.toString()
            binding.interest.text = (currentInstallment.interest * 100).toString() + "% per Month"
            // binding.paidAmount.text = currentInstallment.paidAmount.toString()
            // binding.status.text = currentInstallment.status
        })

        viewModel.currentInstallmentCard.observe(this, Observer { installmentCard ->
            binding.cardDetailsNumber.text = installmentCard.cardNumber
            binding.cardholderName.text = installmentCard.cardholder
            binding.creditLimit.text = installmentCard.limit.toString()
            binding.issuingBank.text = installmentCard.bank
            binding.variant.text = installmentCard.variant
            binding.grade.text = installmentCard.grade
        })

        viewModel.exitStatus.observe(this, Observer {
            when (it) {
                "OK" -> findNavController().navigate(ViewInstallmentDetailsFragmentDirections.actionViewInstallmentDetailsFragmentToViewInstallmentFragment())
            }
        })

        viewModel.confirmDelete.observe(this, Observer {
            if (it == true) {
                AlertDialog.Builder(this.context!!)
                    .setTitle("Delete This Card?")
                    .setMessage("Are you sure you want to delete this installment? This cannot be undone.")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes) { _, _ ->
                        viewModel.deleteInstallment()
                        this.findNavController().navigate(
                            ViewInstallmentDetailsFragmentDirections
                                .actionViewInstallmentDetailsFragmentToViewInstallmentFragment()
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
