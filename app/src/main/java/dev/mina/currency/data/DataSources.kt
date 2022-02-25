package dev.mina.currency.data

import retrofit2.http.GET
import retrofit2.http.Query


interface FixerAPI {

    @GET("symbols")
    suspend fun getSymbols(): Symbols

    @GET("latest")
    suspend fun getLatestRates(
        @Query("base") base: String? = null,
        @Query("symbols") symbols: String? = null,
    ): LatestRates

    @GET("convert")
    suspend fun convert(): Converted
}
