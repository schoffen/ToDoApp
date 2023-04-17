package site.felipeschoffen.todoapp.common

import com.google.firebase.Timestamp
import java.util.*

object DateTimeUtils {
    val todayDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    val todayMonth = Calendar.getInstance().get(Calendar.MONTH)
    val todayYear = Calendar.getInstance().get(Calendar.YEAR)
    val todayHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val todayMinute = Calendar.getInstance().get(Calendar.MINUTE)

    fun dateToString(day: Int, month: Int, year: Int): String {
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

        return "$day $monthString $year"
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
}
