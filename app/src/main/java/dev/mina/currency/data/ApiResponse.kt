package dev.mina.currency.data

import com.google.gson.annotations.SerializedName


data class Symbols(
    @SerializedName("success")
    val success: Boolean? = null,
    @SerializedName("symbols")
    val symbols: Map<String, String>? = null,
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
    val rates: Map<String, Double>? = null,
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
    val result: Double? = null,

    )

data class Query(
    @SerializedName("from")
    val from: String? = null,
    @SerializedName("to")
    val to: String? = null,
    @SerializedName("amount")
    val amount: Int? = null,

    )

data class Info(

    @SerializedName("timestamp")
    val timestamp: Int? = null,
    @SerializedName("rate")
    val rate: Double? = null,

    )