package dev.mina.currency

import java.math.BigDecimal

infix fun String.divideBy(factor: BigDecimal) =
    (toBigDecimalOrNull()?.divide(factor) ?: 0.0).toString()

infix fun String.multiplyBy(factor: BigDecimal) =
    (toBigDecimalOrNull()?.multiply(factor) ?: 0.0).toString()

