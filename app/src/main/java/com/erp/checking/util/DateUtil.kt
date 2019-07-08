package com.erp.checking.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    fun formatDateInbox(date: Date): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(date)
    }
}