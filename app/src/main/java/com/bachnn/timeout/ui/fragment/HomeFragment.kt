package com.bachnn.timeout.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bachnn.timeout.base.BaseFragment
import com.bachnn.timeout.databinding.HomeFragmentBinding
import com.bachnn.timeout.ui.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, HomeFragmentBinding>() {
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
    }
}