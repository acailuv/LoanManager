package com.github.acailuv.loanmanager.view_installment


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
import com.github.acailuv.loanmanager.InstallmentItemAdapter
import com.github.acailuv.loanmanager.R
import com.github.acailuv.loanmanager.ViewModelFactory
import com.github.acailuv.loanmanager.database.AppDatabase
import com.github.acailuv.loanmanager.databinding.FragmentViewInstallmentBinding

/**
 * A simple [Fragment] subclass.
 */
class ViewInstallmentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentViewInstallmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_view_installment, container, false)

        val application = requireNotNull(this.activity).application
        val userDataSource = AppDatabase.getInstance(application).userDao
        val cardDataSource = AppDatabase.getInstance(application).cardDao
        val installmentDataSource = AppDatabase.getInstance(application).installmentDao

        val viewModelFactory =
            ViewModelFactory(userDataSource, cardDataSource, installmentDataSource, application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ViewInstallmentFragmentViewModel::class.java)

        // Recycler View stuff
        val recyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        var recyclerAdapter: InstallmentItemAdapter

        viewModel.installmentList.observe(this, Observer { installmentList ->
            recyclerAdapter = InstallmentItemAdapter(installmentList)
            recyclerView.adapter = recyclerAdapter
            recyclerAdapter.setOnItemClickListener(object :
                InstallmentItemAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    findNavController().navigate(
                        ViewInstallmentFragmentDirections.actionViewInstallmentFragmentToViewInstallmentDetailsFragment(
                            installmentList[position].id
                        )
                    )
                }
            })
        })

        binding.viewModel = viewModel

        // Inflate the layout for this fragment
        return binding.root
    }


}
