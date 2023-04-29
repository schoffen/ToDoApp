package site.felipeschoffen.todoapp.common.util

import android.content.Context
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.datas.TagColors

enum class PriorityTag {
    URGENT {
        override fun getPriorityColor(): TagColors {
            return TagColors(R.color.tag_urgent_red_dark, R.color.tag_urgent_red_light)
        }

        override fun getTagNameInPortuguese(context: Context): String {
            return context.getString(R.string.urgent_portuguese)
        }
    },
    HIGH_PRIORITY {
        override fun getPriorityColor(): TagColors {
            return TagColors(R.color.tag_high_priority_orange_dark, R.color.tag_high_priority_orange_light)
        }

        override fun getTagNameInPortuguese(context: Context): String {
            return context.getString(R.string.high_priority_portuguese)
        }
    },
    MEDIUM_PRIORITY {
        override fun getPriorityColor(): TagColors {
            return TagColors(R.color.tag_medium_priority_yellow_dark, R.color.tag_medium_priority_yellow_light)
        }

        override fun getTagNameInPortuguese(context: Context): String {
            return context.getString(R.string.medium_priority_portuguese)
        }
    },
    LOW_PRIORITY {
        override fun getPriorityColor(): TagColors {
            return TagColors(R.color.tag_low_priority_blue_dark, R.color.tag_low_priority_blue_light)
        }

        override fun getTagNameInPortuguese(context: Context): String {
            return context.getString(R.string.low_priority_portuguese)
        }
    };

    abstract fun getPriorityColor(): TagColors
    abstract fun getTagNameInPortuguese(context: Context): String
}
