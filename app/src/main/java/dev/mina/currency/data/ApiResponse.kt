package dev.mina.currency.data

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Error(
    @SerializedName("code")
    val code: Int? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("info")
    val info: String? = null,
)

data class Symbols(
    @SerializedName("success")
    val success: Boolean? = null,
    @SerializedName("symbols")
    val symbols: Map<String, String>? = null,
    @SerializedName("error")
    val error: Error? = null,
)

data class LatestRates(
    @SerializedName("success")
    val success: Boolean? = null,
    @SerializedName("timestamp")
    val timestamp: Int? = null,
    @SerializedName("base")
    val base: String? = null,
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("rates")
    val rates: Map<String, BigDecimal>? = null,
    @SerializedName("error")
    val error: Error? = null,
)

data class Converted(
    @SerializedName("success")
    val success: Boolean? = null,
    @SerializedName("query")
    val query: Query? = Query(),
    @SerializedName("info")
    val info: Info? = Info(),
    @SerializedName("historical")
    val historical: String? = null,
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("result")
    val result: BigDecimal? = null,
    @SerializedName("error")
    val error: Error? = null,
)

data class Query(
    @SerializedName("from")
    val from: String? = null,
    @SerializedName("to")
    val to: String? = null,
    @SerializedName("amount")
    val amount: Int? = null,
    @SerializedName("error")
    val error: Error? = null,
)

data class Info(
    @SerializedName("timestamp")
    val timestamp: Int? = null,
    @SerializedName("rate")
    val rate: BigDecimal? = null,
    @SerializedName("error")
    val error: Error? = null,
)

data class HistoricRate(
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("historical")
    var historical: Boolean? = null,
    @SerializedName("date")
    var date: String? = null,
    @SerializedName("timestamp")
    var timestamp: Int? = null,
    @SerializedName("base")
    var base: String? = null,
    @SerializedName("rates")
    val rates: Map<String, BigDecimal>? = null,
)

data class TimeSeriesRates(
    @SerializedName("success")
    val success: Boolean? = null,
    @SerializedName("timeseries")
    val timeSeries: Boolean? = null,
    @SerializedName("start_date")
    val startDate: String? = null,
    @SerializedName("end_date")
    val endDate: String? = null,
    @SerializedName("base")
    val base: String? = null,
    @SerializedName("rates")
    val rates: Map<String, Map<String, BigDecimal>>? = null,
)