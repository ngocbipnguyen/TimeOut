package com.bachnn.timeout.utilities

import android.content.Context
import android.preference.PreferenceManager

class SharedPreferenceUnit {
    companion object {
        fun getInt(context: Context, name: String, defaultValue: Int): Int {
            val sp =  PreferenceManager.getDefaultSharedPreferences(context);
            return sp.getInt(name, defaultValue)
        }


        fun setInt(context: Context, name: String, defaultValue: Int) {
            val sp =  PreferenceManager.getDefaultSharedPreferences(context);
            val edit = sp.edit()
            edit.putInt(name, defaultValue)
            edit.apply()
        }

    }
}