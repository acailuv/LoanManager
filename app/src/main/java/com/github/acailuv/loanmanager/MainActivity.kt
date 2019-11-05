package com.github.acailuv.loanmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.github.acailuv.loanmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        var cards = listOf<String>(
            "Food Card - VISA",
            "Shopping Card - MasterCard",
            "Bills Card - VISA"
        )

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, cards)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        binding.cardSelectSpinner!!.adapter = aa

        /**
         * TODO: Card Entry
         * > Card Number
         * > Name
         * > Bank
         * > Variant (Ex. BNI Titanium Style != BNI Garuda Indonesia != BNI Platinum)
         * > Billing Cycle
         * > Terms of Payment (Due date to pay for stuff. Ex: 5 days away from billing cycle)
         * > Credit Limit
         */

        // Installment page

    }
}
