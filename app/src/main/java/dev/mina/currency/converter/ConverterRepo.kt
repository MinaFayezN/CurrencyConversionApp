package dev.mina.currency.converter

import dev.mina.currency.data.FixerAPI
import dev.mina.currency.data.LatestRates
import dev.mina.currency.data.Symbols
import javax.inject.Inject

interface ConverterRepo {
    suspend fun getSymbols(): Symbols
    suspend fun getLatestRates(
        base: String? = null,
        symbols: List<String>? = null,
    ): LatestRates
}

class ConverterRepoImpl @Inject constructor(private val dataSource: FixerAPI) : ConverterRepo {
    override suspend fun getSymbols(): Symbols = dataSource.getSymbols()
    override suspend fun getLatestRates(base: String?, symbols: List<String>?): LatestRates =
        dataSource.getLatestRates(base = base, symbols = symbols?.toParams())
}

fun List<String>.toParams(): String {
    return toString().removeSurrounding("[","]")
}