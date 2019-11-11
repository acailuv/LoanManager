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
import com.github.acailuv.loanmanager.R
import com.github.acailuv.loanmanager.ViewModelFactory
import com.github.acailuv.loanmanager.database.AppDatabase
import com.github.acailuv.loanmanager.databinding.FragmentDashboardBinding
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.DataPoint


/**
 * TODO(1) Monthly Loan
 * TODO(2) Graph
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

        val viewModelFactory = ViewModelFactory(
            userDataSource,
            cardDataSource,
            installmentDataSource,
            application
        )
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(DashboardFragmentViewModel::class.java)

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

        // Graph Testing
        val graph = binding.graph
        graph.title = "Installment Vs. Income"
        graph.viewport.setScrollableY(true)

        val series = LineGraphSeries<DataPoint>(
            arrayOf(
                DataPoint(0.0, -2.0),
                DataPoint(1.0, 5.0),
                DataPoint(2.0, 3.0),
                DataPoint(3.0, 2.0),
                DataPoint(4.0, 6.0),
                DataPoint(5.0, -2.0),
                DataPoint(6.0, 5.0),
                DataPoint(7.0, 3.0),
                DataPoint(8.0, 2.0),
                DataPoint(9.0, 6.0)
            )
        )
        series.color = Color.GREEN
        series.title = "Income"
        graph.addSeries(series)

        val series2 = LineGraphSeries<DataPoint>(
            arrayOf(
                DataPoint(0.0, 3.0),
                DataPoint(1.0, 3.0),
                DataPoint(2.0, 6.0),
                DataPoint(3.0, 2.0),
                DataPoint(4.0, 5.0),
                DataPoint(5.0, 3.0),
                DataPoint(6.0, 3.0),
                DataPoint(7.0, 6.0),
                DataPoint(8.0, 2.0),
                DataPoint(9.0, 5.0)
            )
        )
        series2.color = Color.RED
        series2.title = "Installments"
        graph.addSeries(series2)
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
