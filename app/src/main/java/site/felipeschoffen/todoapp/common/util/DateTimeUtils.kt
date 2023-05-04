package site.felipeschoffen.todoapp.common.util

import android.content.Context
import com.google.firebase.Timestamp
import site.felipeschoffen.todoapp.R
import java.util.*

object DateTimeUtils {
    val todayDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    val todayMonth = Calendar.getInstance().get(Calendar.MONTH)
    val todayYear = Calendar.getInstance().get(Calendar.YEAR)
    val todayHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val todayMinute = Calendar.getInstance().get(Calendar.MINUTE)

    fun dateToString(day: Int, month: Int, year: Int): String {
        val dayString = if (day < 10) "0$day" else day

        val monthString = when (month) {
            0 -> "JAN"
            1 -> "FEV"
            2 -> "MAR"
            3 -> "ABR"
            4 -> "MAI"
            5 -> "JUN"
            6 -> "JUL"
            7 -> "AGO"
            8 -> "SET"
            9 -> "OUT"
            10 -> "NOV"
            11 -> "DEZ"
            else -> ""
        }

        return "$dayString $monthString $year"
    }

    fun intToTimestamp(hour: Int, minute: Int): Timestamp {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        return Timestamp(Date(calendar.timeInMillis))
    }

    fun formatTime(timestamp: Timestamp): String {
        val hour = timestamp.toDate().hours
        val minute = timestamp.toDate().minutes

        val newHour = if (hour < 10) "0$hour" else hour
        val newMinute = if (minute < 10) "0$minute" else minute

        return "$newHour:$newMinute"
    }

    fun formatDate(timestamp: Timestamp): String {
        val calendar = Calendar.getInstance()
        calendar.time = timestamp.toDate()

        val day =
            if (calendar.get(Calendar.DAY_OF_MONTH) < 10)
                "0${calendar.get(Calendar.DAY_OF_MONTH)}"
            else
                calendar.get(Calendar.DAY_OF_MONTH)

        val month = when (calendar.get(Calendar.MONTH)) {
            0 -> "JAN"
            1 -> "FEV"
            2 -> "MAR"
            3 -> "ABR"
            4 -> "MAI"
            5 -> "JUN"
            6 -> "JUL"
            7 -> "AGO"
            8 -> "SET"
            9 -> "OUT"
            10 -> "NOV"
            11 -> "DEZ"
            else -> ""
        }

        val year = calendar.get(Calendar.YEAR)

        return "$day $month $year"
    }
}
