package com.weit2nd.presentation.util

import java.time.format.DateTimeFormatter

object LocalDateTimeFormatter {
    val reviewDateFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy.MM.dd")
}
