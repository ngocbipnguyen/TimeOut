package com.bachnn.timeout.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bachnn.timeout.base.BaseFragment
import com.bachnn.timeout.databinding.SplashFragmentBinding
import com.bachnn.timeout.ui.viewModel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<SplashViewModel, SplashFragmentBinding>() {
    override fun createViewModel(): SplashViewModel {
        return ViewModelProvider(this)[SplashViewModel::class.java]
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SplashFragmentBinding {
        return SplashFragmentBinding.inflate(inflater, container, false)
    }

    override fun initView() {
    }
}