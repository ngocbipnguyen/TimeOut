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


    fun formatTimestamp() : String {
        if (timestamp.toInt() == 0) {
            return "00:00:00"
        }
        val seconds = (timestamp / 1000) % 60
        val minutes = (timestamp / 1000 / 60) % 60
        val hour = (timestamp / 1000 / 60) / 60
        val strSecond =  if (seconds >= 10) {
            seconds.toString()
        } else {
            "0${seconds}"
        }

        val strMinutes =  if (minutes >= 10) {
            minutes.toString()
        } else {
            "0${minutes}"
        }

        val strHour =  if (hour >= 10) {
            hour.toString()
        } else {
            "0${hour}"
        }
        return "$strHour:$strMinutes:$strSecond"
    }

}