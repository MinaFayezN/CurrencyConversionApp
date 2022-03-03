package dev.mina.currency.details

import dev.mina.currency.data.FixerAPI
import dev.mina.currency.data.HistoricRate
import dev.mina.currency.data.TimeSeriesRates
import javax.inject.Inject
import javax.inject.Named

interface DetailsRepo {
    suspend fun getHistoricalRates(base: String?, symbols: String?): HistoricRate
    suspend fun getTimeSeriesRates(base: String?, symbols: String?): TimeSeriesRates
}

class DetailsRepoImpl @Inject constructor(
    private val dataSource: FixerAPI,
    @Named("CurrentDate")
    private val currentDate: String,
    @Named("LastMonthDate")
    private val prevMonthDate: String,
) :
    DetailsRepo {
    override suspend fun getHistoricalRates(base: String?, symbols: String?): HistoricRate =
        dataSource.getHistoricalRates(date = prevMonthDate, base = base, symbols = symbols)

    override suspend fun getTimeSeriesRates(base: String?, symbols: String?): TimeSeriesRates =
        dataSource.getTimeSeriesRates(
            prevMonth = prevMonthDate,
            currentDate = currentDate,
            base = base,
            symbols = symbols)

}