package com.github.acailuv.loanmanager.dashboard


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.acailuv.loanmanager.R
import com.github.acailuv.loanmanager.add_card.AddCardFragmentViewModel
import com.github.acailuv.loanmanager.database.AppDatabase
import com.github.acailuv.loanmanager.databinding.FragmentDashboardBinding

/**
 * TODO(1) Monthly Loan
 * TODO(3) Graph
 */

class DashboardFragment : Fragment() {

    lateinit var viewModel: DashboardFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentDashboardBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)

        val application = requireNotNull(this.activity).application
        val userDataSource = AppDatabase.getInstance(application).userDao
        val cardDataSource = AppDatabase.getInstance(application).cardDao
        val installmentDataSource = AppDatabase.getInstance(application).installmentDao

        val viewModelFactory = DashboardFragmentViewModelFactory(
            userDataSource,
            cardDataSource,
            installmentDataSource,
            application
        )
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(DashboardFragmentViewModel::class.java)

        viewModel.initializeUserInfo()
        viewModel.initializeSpinnerContent()

        viewModel.spinnerContent.observe(this, Observer {
            if (it.isEmpty()) {
                binding.cardDetailsContainer.visibility = View.INVISIBLE
                binding.cardDetailsHeader.visibility = View.INVISIBLE

            } else {

                val arrayAdapter =
                    ArrayAdapter(this.context!!, android.R.layout.simple_spinner_item, it)
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
                            // Update view value accordingly
                            binding.cardDetailsHeader.text =
                                binding.cardSelectSpinner.selectedItem.toString()
                            binding.cardDetailsNumber.text =
                                viewModel.rawSpinnerContent.value?.get(position)?.cardNumber
                            binding.cardholderName.text =
                                viewModel.rawSpinnerContent.value?.get(position)?.cardholder
                            binding.creditLimit.text =
                                viewModel.rawSpinnerContent.value?.get(position)?.limit.toString()
                            binding.issuingBank.text =
                                viewModel.rawSpinnerContent.value?.get(position)?.bank
                            binding.variant.text =
                                viewModel.rawSpinnerContent.value?.get(position)?.variant
                            binding.grade.text =
                                viewModel.rawSpinnerContent.value?.get(position)?.grade
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // Do absolutely nothing
                        }
                    }

            }
        })

        viewModel.user.observe(this, Observer {
            binding.userName.text = it.name
            binding.monthlyIncome.text = it.monthlyIncome.toString()
        })

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.initializeSpinnerContent()
    }

}
