package site.felipeschoffen.todoapp.common

import java.util.Calendar

object Date {
    val todayDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    val todayMonth = Calendar.getInstance().get(Calendar.MONTH)
    val todayYear = Calendar.getInstance().get(Calendar.YEAR)
    val todayHour = Calendar.getInstance().get(Calendar.HOUR)
    val todayMinute = Calendar.getInstance().get(Calendar.MINUTE)

    fun dateToString(day: Int, month: Int, year: Int) : String {
        val monthString = when(month) {
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
}
