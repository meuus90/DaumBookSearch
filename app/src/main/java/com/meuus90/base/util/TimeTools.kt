package com.meuus90.base.util

import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TimeTools {
    companion object {
        fun convertDateFormat(
            inputDate: String,
            inputFormat: String,
            outputFormat: String
        ): String {
            var outputDate = ""

            val input = SimpleDateFormat(inputFormat, Locale.getDefault())
            val output = SimpleDateFormat(outputFormat, Locale.getDefault())

            try {
                val parsed = input.parse(inputDate)

//                Timber.d(parsed.toString())
                parsed?.let {
                    outputDate = output.format(parsed)
                }
            } catch (e: ParseException) {
                Timber.d(e.stackTrace.toString())
            }
//            Timber.d(outputDate)

            return outputDate
        }

        const val YMD = "yyyy.MM.dd."
        const val ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS"
//        "2012-01-10T00:00:00.000+09:00"
    }
}