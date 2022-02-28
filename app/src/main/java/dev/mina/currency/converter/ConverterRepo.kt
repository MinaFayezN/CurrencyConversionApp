package dev.mina.currency.converter

import dev.mina.currency.data.Converted
import dev.mina.currency.data.FixerAPI
import dev.mina.currency.data.LatestRates
import dev.mina.currency.data.Symbols
import java.math.BigDecimal
import javax.inject.Inject

interface ConverterRepo {
    suspend fun getSymbols(): Symbols
    suspend fun getLatestRates(
        base: String? = null,
        symbols: List<String>? = null,
    ): LatestRates

    suspend fun convert(
        from: String? = null,
        to: String? = null,
        amount: BigDecimal? = null,
    ): Converted
}

class ConverterRepoImpl @Inject constructor(private val dataSource: FixerAPI) : ConverterRepo {
    override suspend fun getSymbols(): Symbols = dataSource.getSymbols()

    override suspend fun getLatestRates(base: String?, symbols: List<String>?): LatestRates =
        dataSource.getLatestRates(base = base, symbols = symbols?.toParams())

    override suspend fun convert(from: String?, to: String?, amount: BigDecimal?): Converted =
        dataSource.convert(from = from, to = to, amount = amount)

}

fun List<String>.toParams(): String {
    return toString().removeSurrounding("[", "]")
}