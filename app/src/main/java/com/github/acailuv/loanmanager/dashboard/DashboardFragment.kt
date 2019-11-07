package com.github.acailuv.loanmanager.dashboard


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.github.acailuv.loanmanager.R
import com.github.acailuv.loanmanager.databinding.FragmentDashboardBinding

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentDashboardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        var cards = listOf(
            "Food Card - VISA",
            "Shopping Card - MasterCard",
            "Bills Card - VISA"
        )

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this.context!!, android.R.layout.simple_spinner_item, cards)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        binding.cardSelectSpinner.adapter = aa

        // Inflate the layout for this fragment
        return binding.root
    }


}
