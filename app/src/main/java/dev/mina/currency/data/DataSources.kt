package dev.mina.currency.data

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import java.math.BigDecimal


interface FixerAPI {

    @GET("symbols")
    suspend fun getSymbols(): Symbols

    @GET("latest")
    suspend fun getLatestRates(
        @Query("base")
        base: String? = null,
        @Query("symbols")
        symbols: String? = null,
    ): LatestRates

    @GET("convert")
    suspend fun convert(
        @Query("from")
        from: String? = null,
        @Query("to")
        to: String? = null,
        @Query("amount")
        amount: BigDecimal? = null,
    ): Converted

    @GET
    suspend fun getHistoricalRates(
        @Url
        date: String,
        @Query("base")
        base: String? = null,
        @Query("symbols")
        symbols: String? = null,
    ): HistoricRate

    @GET("timeseries")
    suspend fun getTimeSeriesRates(
        @Query("start_date")
        prevMonth: String? = null,
        @Query("end_date")
        currentDate: String? = null,
        @Query("base")
        base: String? = null,
        @Query("symbols")
        symbols: String? = null,
    ): TimeSeriesRates


}
