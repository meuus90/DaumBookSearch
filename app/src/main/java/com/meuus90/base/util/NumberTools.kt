package com.meuus90.base.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

class NumberTools {
    companion object {
        fun convertToString(amount: BigDecimal): String {
            if (amount.compareTo(0.toBigDecimal()) == 0)
                return "0"

            val newAmount = amount.setCustomScale(amount.scale(), RoundingMode.DOWN)

            val format = NumberFormat.getNumberInstance()
            format.maximumFractionDigits = amount.scale()
//            format.minimumFractionDigits = scale

            return format.format(newAmount)
        }

        fun BigDecimal.customStripTrailingZeros(): BigDecimal {
            return if (scale() <= 0) {
                this
            } else {
                stripTrailingZeros()
            }
        }

        fun BigDecimal.setCustomScale(
            scale: Int = 0,
            roundingMode: RoundingMode = RoundingMode.DOWN
        ): BigDecimal {
            return this.setScale(scale, roundingMode).customStripTrailingZeros()
        }
    }
}