package com.bachnn.timeout.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bachnn.timeout.R
import com.bachnn.timeout.base.BaseFragment
import com.bachnn.timeout.data.model.AppInfo
import com.bachnn.timeout.databinding.InformationFragmentBinding
import com.bachnn.timeout.ui.viewModel.InformationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InformationFragment : BaseFragment<InformationViewModel, InformationFragmentBinding>() {

    private var appInfo: AppInfo? = null

    override fun createViewModel(): InformationViewModel {
        return ViewModelProvider(this)[InformationViewModel::class.java]
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): InformationFragmentBinding {
        return InformationFragmentBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        try {
            appInfo = InformationFragmentArgs.fromBundle(requireArguments()).appInfo
            
            if (appInfo == null) {
                Log.e("InformationFragment", "AppInfo is null from arguments")
                return
            }

            viewModel.setAppInfo(appInfo)
            
            binding.apply {
                infoViewModel = viewModel
                lifecycleOwner = viewLifecycleOwner
            }

            binding.enableLinear.setOnClickListener {
                // todo:
                showDialog()
            }
            
            Log.d("InformationFragment", "Initialized with appInfo: $appInfo")
        } catch (e: Exception) {
            Log.e("InformationFragment", "Error initializing fragment", e)
        }
    }

    private fun showDialog() {
        val notificationMessage = if (viewModel.isAppEnabled()) {
            getString(R.string.disable_message_app)
        } else {
            getString(R.string.enable_message_app)
        }

        val alertDialog =  AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.title_notification_info))
            .setMessage(notificationMessage)
            .setPositiveButton("Ok"
            ) { dialog, _ ->
                //todo enable app
                dialog.cancel()
            }
            .setNegativeButton("Cancel"
            ) { dialog, _ ->
                //todo disable app
                dialog.cancel()
            }.create()
        alertDialog.show()
    }
}