package com.bachnn.timeout.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.bachnn.timeout.R
import com.bachnn.timeout.adapter.HomeAdapter
import com.bachnn.timeout.base.BaseFragment
import com.bachnn.timeout.databinding.HomeFragmentBinding
import com.bachnn.timeout.ui.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, HomeFragmentBinding>() {

    lateinit var adapter: HomeAdapter

    override fun createViewModel(): HomeViewModel {
        return ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): HomeFragmentBinding {
        return HomeFragmentBinding.inflate(inflater, container, false)
    }

    override fun initView() {

        adapter = HomeAdapter(clickListener = {
            val navController = binding.root.findNavController()
//            navController.navigate(R.navigation.)
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.homeFragment, false)
                .build()

            navController.navigate(R.id.action_homeFragment_to_informationFragment,
                Bundle().apply {
                    putSerializable("appInfo", it)
                },
                navOptions
            )
        })

        binding.packageRecycle.adapter = adapter

        viewModel.packageInfo.observe(viewLifecycleOwner, Observer {it ->
            if (it.isNotEmpty()) {
                binding.progressCircular.visibility = View.GONE
                binding.packageRecycle.visibility = View.VISIBLE
            }
            adapter.submitList(it)
        })

    }
}