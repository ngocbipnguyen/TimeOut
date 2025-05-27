package com.bachnn.timeout.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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