package com.bachnn.timeout.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppInfo(
    @PrimaryKey val packageName: String,
    val label: String,
    val target: Int,
    val timestamp: Long = 0
): java.io.Serializable {
    fun getStrTarget() : String = target.toString()
}