package com.github.acailuv.loanmanager.manage_card


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.acailuv.loanmanager.CardItemAdapter
import com.github.acailuv.loanmanager.R
import com.github.acailuv.loanmanager.ViewModelFactory
import com.github.acailuv.loanmanager.database.AppDatabase
import com.github.acailuv.loanmanager.databinding.FragmentManageCardBinding

class ManageCardFragment : Fragment() {

    lateinit var binding: FragmentManageCardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_card, container, false)

        val application = requireNotNull(this.activity).application
        val userDataSource = AppDatabase.getInstance(application).userDao
        val cardDataSource = AppDatabase.getInstance(application).cardDao
        val installmentDataSource = AppDatabase.getInstance(application).installmentDao

        val viewModelFactory =
            ViewModelFactory(userDataSource, cardDataSource, installmentDataSource, application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ManageCardFragmentViewModel::class.java)


        // Recycler View stuff
        val recyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        var recyclerAdapter: CardItemAdapter

        viewModel.cardList.observe(this, Observer { cardList ->
            recyclerAdapter = CardItemAdapter(cardList)
            recyclerView.adapter = recyclerAdapter
            recyclerAdapter.setOnItemClickListener(object : CardItemAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    findNavController().navigate(
                        ManageCardFragmentDirections.actionManageCardFragmentToChangeCardDetailsFragment(
                            cardList[position].id
                        )
                    )
                }
            })
        })

        // Inflate the layout for this fragment
        return binding.root
    }


}
