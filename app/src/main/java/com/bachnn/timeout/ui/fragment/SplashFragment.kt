package com.bachnn.timeout.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.bachnn.timeout.R
import com.bachnn.timeout.base.BaseFragment
import com.bachnn.timeout.databinding.SplashFragmentBinding
import com.bachnn.timeout.ui.viewModel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        val navController = binding.root.findNavController()

        lifecycleScope.launch {

            delay(3000)

            //todo check is firstly open app.
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.splashFragment, true)
                .build()

            navController.navigate(R.id.action_splashFragment_to_homeFragment,
                Bundle(),
                navOptions
            )
        }

    }
}