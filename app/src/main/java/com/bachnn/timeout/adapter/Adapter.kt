package com.bachnn.timeout.adapter

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("imageDrawable")
fun setImageDrawableByPackage(imageView: ImageView, packageName: String?) {
    if (packageName == null) {
        Log.e("setImageDrawableByPackage", "Package name is null")
        return
    }
    
    try {
        val drawable = imageView.context.packageManager.getApplicationIcon(packageName)
        imageView.setImageDrawable(drawable)
    } catch (e: Exception) {
        Log.e("setImageDrawableByPackage", "Error setting image for package: $packageName", e)
    }
}

