package com.github.acailuv.loanmanager.dashboard


import android.graphics.Color
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
import androidx.navigation.fragment.findNavController
import com.github.acailuv.loanmanager.R
import com.github.acailuv.loanmanager.ViewModelFactory
import com.github.acailuv.loanmanager.database.AppDatabase
import com.github.acailuv.loanmanager.databinding.FragmentDashboardBinding
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.DataPoint

class DashboardFragment : Fragment() {

    lateinit var binding: FragmentDashboardBinding
    lateinit var viewModel: DashboardFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)


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
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(DashboardFragmentViewModel::class.java)

        binding.editUserData.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToChangeUserDataFragment())
            viewModel.initializeUserInfo()
        }

        viewModel.monthlyLoan.observe(this, Observer { monthlyLoan ->
            binding.aboutYouPaymentDue.text = monthlyLoan.toString()
        })

        viewModel.spinnerContent.observe(this, Observer {
            if (it.isEmpty()) {
                binding.cardDetailsContainer.visibility = View.INVISIBLE
                binding.cardDetailsHeader.visibility = View.INVISIBLE
                binding.cardSelectSpinner.adapter = null

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
                            viewModel.getTotalLoan(viewModel.rawSpinnerContent.value?.get(position))
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // Do absolutely nothing
                        }
                    }

            }
        })

        viewModel.totalLoanInCard.observe(this, Observer { totalLoanInCard ->
            binding.cardDetailsLoan.text = totalLoanInCard.toString()
        })

        viewModel.user.observe(this, Observer {
            binding.userName.text = it.name
            binding.monthlyIncome.text = it.monthlyIncome.toString()
        })

        // Graph Testing
        val graph = binding.graph
        graph.title = "Installment Vs. Income"
        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.isScalable = true
        graph.viewport.isScrollable = true

        viewModel.buildGraphSeries()

        viewModel.monthlyIncomeSeries.observe(this, Observer {
            val incomeSeries = LineGraphSeries<DataPoint>(it)
            incomeSeries.color = Color.GREEN
            incomeSeries.title = "Income"
            if (graph.series.size > 0) graph.removeAllSeries()
            graph.addSeries(incomeSeries)
        })

        viewModel.monthlyLoanSeries.observe(this, Observer {
            val monthlyLoanSeries = LineGraphSeries<DataPoint>(it)
            monthlyLoanSeries.color = Color.RED
            monthlyLoanSeries.title = "Installments"
            graph.addSeries(monthlyLoanSeries)
        })

        graph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(activity)
        graph.gridLabelRenderer.numHorizontalLabels = 3

        viewModel.lowestDate.observe(this, Observer {
            graph.viewport.setMinX(it.time.toDouble())
        })

        viewModel.highestDate.observe(this, Observer {
            graph.viewport.setMaxX(it.time.toDouble())
        })

        graph.legendRenderer.isVisible = true
        graph.legendRenderer.align = LegendRenderer.LegendAlign.BOTTOM

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
