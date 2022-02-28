package dev.mina.currency.data

import retrofit2.http.GET
import retrofit2.http.Query
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
}
