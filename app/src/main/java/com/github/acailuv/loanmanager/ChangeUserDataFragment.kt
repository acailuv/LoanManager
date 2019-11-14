package com.github.acailuv.loanmanager


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.github.acailuv.loanmanager.database.AppDatabase
import com.github.acailuv.loanmanager.databinding.FragmentChangeUserDataBinding


class ChangeUserDataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentChangeUserDataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_change_user_data, container, false)

        val application = requireNotNull(this.activity).application
        val userDataSource = AppDatabase.getInstance(application).userDao
        val cardDataSource = AppDatabase.getInstance(application).cardDao
        val installmentDataSource = AppDatabase.getInstance(application).installmentDao

        val viewModelFactory =
            ViewModelFactory(userDataSource, cardDataSource, installmentDataSource, application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ChangeUserDataFragmentViewModel::class.java)

        viewModel.exitStatus.observe(this, Observer { exitStatus ->
            when (exitStatus) {
                "OK" -> {
                    findNavController().navigate(ChangeUserDataFragmentDirections.actionChangeUserDataFragmentToDashboardFragment())
                    viewModel.clean()
                }
            }
        })

        binding.viewModel = viewModel

        // Inflate the layout for this fragment
        return binding.root
    }


}
