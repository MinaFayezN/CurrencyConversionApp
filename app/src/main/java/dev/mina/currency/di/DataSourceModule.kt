package dev.mina.currency.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mina.currency.BuildConfig
import dev.mina.currency.data.FixerAPI
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Singleton
    @Provides
    @Named("CurrentDate")
    fun provideCurrentDate(): String = calculateDate(RequiredMonth.CURRENT)

    @Singleton
    @Provides
    @Named("LastMonthDate")
    fun provideDateMinusMonth(): String = calculateDate(RequiredMonth.PREVIOUS)

    private fun calculateDate(requiredMonth: RequiredMonth): String {
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.MONTH, requiredMonth.value)
        val format = SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH)
        return format.format(cal.time)
    }


    //region Retrofit
    @Singleton
    @Provides
    @Named("BaseUrl")
    fun provideBaseUrl() = "https://data.fixer.io/api/"

    @Singleton
    @Provides
    @Named("MockInterceptor")
    fun providesMockInterceptor() = MockInterceptor(provideDateMinusMonth())

    @Singleton
    @Provides
    @Named("LoggingInterceptor")
    fun providesLoggingInterceptor() = HttpLoggingInterceptor()
        .apply { level = HttpLoggingInterceptor.Level.BODY }

    @Singleton
    @Provides
    @Named("KeyInterceptor")
    fun providesKeyInterceptor() = Interceptor { chain ->
        val original = chain.request()
        val originalHttpUrl = original.url
        val url = originalHttpUrl
            .newBuilder()
            .addQueryParameter("access_key", BuildConfig.apiKey) // PLease add the Key on your local.properties file or use mocked responses instead
            .build()
        val requestBuilder = original.newBuilder().url(url)
        val request = requestBuilder.build()
        chain.proceed(request)
    }

    @Singleton
    @Provides
    @Named("Client")
    fun providesOkHttpClient(
        @Named("MockInterceptor") mockInterceptor: MockInterceptor,
        @Named("LoggingInterceptor") loggingInterceptor: HttpLoggingInterceptor,
        @Named("KeyInterceptor") keyInterceptor: Interceptor,
    ) = OkHttpClient
        .Builder()
        .addInterceptor(mockInterceptor) // Use this if you want to mock responses instead of getting response from API
        .addInterceptor(loggingInterceptor)
        .addInterceptor(keyInterceptor)
        .build()


    @Singleton
    @Provides
    fun provideRetrofit(
        @Named("BaseUrl") baseUrl: String,
        @Named("Client") client: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }

    @Singleton
    @Provides
    fun provideUserAPI(retrofit: Retrofit): FixerAPI =
        retrofit.create(FixerAPI::class.java)
    //endregion
}

class MockInterceptor @Inject constructor(
    @Named("LastMonthDate")
    private val date: String?,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val uri = chain.request().url.toUri().toString()
        val responseString = when {
            uri.startsWith("https://data.fixer.io/api/symbols") -> MOCKED_SYMBOLS
            uri.startsWith("https://data.fixer.io/api/latest") -> MOCKED_LATEST_RATES
            uri.startsWith("https://data.fixer.io/api/convert") -> MOCKED_CONVERT
            uri.startsWith("https://data.fixer.io/api/timeseries") -> MOCKED_TIME_SERIES
            uri.startsWith("https://data.fixer.io/api/$date") -> MOCKED_HISTORICAL
            else -> ""
        }

        return chain.proceed(chain.request())
            .newBuilder()
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message(responseString)
            .body(responseString.toByteArray()
                .toResponseBody("application/json".toMediaTypeOrNull()))
            .addHeader("content-type", "application/json")
            .build()
    }
}

enum class RequiredMonth(val value: Int) {
    CURRENT(0),
    PREVIOUS(-1)
}

private const val MOCKED_CONVERT = "{\n" +
        "    \"success\": true,\n" +
        "    \"query\": {\n" +
        "        \"from\": \"GBP\",\n" +
        "        \"to\": \"JPY\",\n" +
        "        \"amount\": 25\n" +
        "    },\n" +
        "    \"info\": {\n" +
        "        \"timestamp\": 1519328414,\n" +
        "        \"rate\": 148.972231\n" +
        "    },\n" +
        "    \"historical\": \"\",\n" +
        "    \"date\": \"2018-02-22\",\n" +
        "    \"result\": 3724.305775\n" +
        "}"
private const val MOCKED_LATEST_RATES = "{\n" +
        "    \"success\": true,\n" +
        "    \"timestamp\": 1519296206,\n" +
        "    \"base\": \"USD\",\n" +
        "    \"date\": \"2022-02-24\",\n" +
        "    \"rates\": {\n" +
        "        \"GBP\": 0.72007,\n" +
        "        \"JPY\": 107.346001,\n" +
        "        \"ALL\": 17.346001,\n" +
        "        \"AFN\": 0.813399,\n" +
        "        \"AED\": 0.8145499\n" +
        "    }\n" +
        "}"
private const val MOCKED_SYMBOLS = "{\n" +
        "  \"success\": true,\n" +
        "  \"symbols\": {\n" +
        "    \"AED\": \"United Arab Emirates Dirham\",\n" +
        "    \"AFN\": \"Afghan Afghani\",\n" +
        "    \"ALL\": \"Albanian Lek\",\n" +
        "    \"AMD\": \"Armenian Dram\"\n" +
        "    }\n" +
        "}"

private const val MOCKED_HISTORICAL = "{\n" +
        "    \"success\": true,\n" +
        "    \"historical\": true,\n" +
        "    \"date\": \"2013-12-24\",\n" +
        "    \"timestamp\": 1387929599,\n" +
        "    \"base\": \"GBP\",\n" +
        "    \"rates\": {\n" +
        "        \"USD\": 1.636492,\n" +
        "        \"EUR\": 1.196476,\n" +
        "        \"CAD\": 1.739516\n" +
        "    }\n" +
        "}"

private const val MOCKED_TIME_SERIES = "{\n" +
        "    \"success\": true,\n" +
        "    \"timeseries\": true,\n" +
        "    \"start_date\": \"2012-05-01\",\n" +
        "    \"end_date\": \"2012-05-03\",\n" +
        "    \"base\": \"AED\",\n" +
        "    \"rates\": {\n" +
        "        \"2012-05-01\":{\n" +
        "          \"AFN\": 1.322891,\n" +
        "          \"AUD\": 1.278047,\n" +
        "          \"CAD\": 1.302303\n" +
        "        },\n" +
        "        \"2012-05-02\": {\n" +
        "          \"AFN\": 1.315066,\n" +
        "          \"AUD\": 1.274202,\n" +
        "          \"CAD\": 1.299083\n" +
        "        },\n" +
        "        \"2012-05-03\": {\n" +
        "          \"AFN\": 1.314491,\n" +
        "          \"AUD\": 1.280135,\n" +
        "          \"CAD\": 1.296868\n" +
        "        }\n" +
        "    }\n" +
        "}"







