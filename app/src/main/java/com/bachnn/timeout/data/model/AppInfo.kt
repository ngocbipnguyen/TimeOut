package com.bachnn.timeout.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppInfo(
    @PrimaryKey val packageName: String,
    val label: String,
    val target: Int,
    var timestamp: Long = 0,
    var active: Boolean = false
): java.io.Serializable {
    fun getStrTarget() : String = target.toString()
    override fun toString(): String {
        return "AppInfo(packageName='$packageName', label='$label', target=$target, timestamp=$timestamp, active=$active)"
    }


}