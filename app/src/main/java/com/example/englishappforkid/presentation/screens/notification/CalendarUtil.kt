package com.example.englishappforkid.utils

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import com.example.englishappforkid.R
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun openGoogleCalendar(
    context: Context,
    title: String,
    description: String,
    startDate: String,
    endDate: String,
    frequency: String,
) {
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val startMillis = dateFormat.parse(startDate)?.time ?: return
    val endMillis = dateFormat.parse(endDate)?.time ?: return

    // Map frequency sang RRULE dùng string resource
    val rrule =
        when (frequency) {
            context.getString(R.string.once_everyday) -> "FREQ=DAILY"
            context.getString(R.string.twice_a_day) -> "FREQ=DAILY;INTERVAL=2"
            context.getString(R.string.weekly) -> "FREQ=WEEKLY"
            else -> null
        }

    val intent =
        Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, title)
            putExtra(CalendarContract.Events.DESCRIPTION, description)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
            putExtra(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
            if (rrule != null) putExtra(CalendarContract.Events.RRULE, rrule)
        }
    context.startActivity(intent)
}
