package dev.mina.currency.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    //region Retrofit
    @Singleton
    @Provides
    @Named("BaseUrl")
    fun provideBaseUrl() = "https://data.fixer.io/api/"

    @Singleton
    @Provides
    @Named("MockInterceptor")
    fun providesMockInterceptor() = MockInterceptor()

    @Singleton
    @Provides
    @Named("LoggingInterceptor")
    fun providesLoggingInterceptor() = HttpLoggingInterceptor()
        .apply { level = HttpLoggingInterceptor.Level.BODY }

    @Singleton
    @Provides
    @Named("KeyInterceptor")
    fun providesKeyInterceptor() = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val originalHttpUrl = original.url
            val url = originalHttpUrl
                .newBuilder()
                .addQueryParameter("access_key", "API_KEY")
                .build()
            val requestBuilder = original.newBuilder().url(url)
            val request = requestBuilder.build()
            return chain.proceed(request)
        }
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
        .addInterceptor(mockInterceptor)
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

class MockInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val uri = chain.request().url.toUri().toString()
        val responseString = when {
            uri.startsWith("https://data.fixer.io/api/symbols") -> MOCKED_SYMBOLS
            uri.startsWith("https://data.fixer.io/api/latest") -> MOCKED_LATEST_RATES
            uri.startsWith("https://data.fixer.io/api/convert") -> MOCKED_CONVERT
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
        "        \"EUR\": 0.813399\n" +
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







