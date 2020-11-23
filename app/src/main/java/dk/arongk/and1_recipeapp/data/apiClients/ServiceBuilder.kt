package dk.arongk.and1_recipeapp.data.apiClients

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    // NOTE: this client will have headers specific to the edamam nutritional api, take this
    // into account if more endpoints should be consumed in the future
    private val client = OkHttpClient
        .Builder()
        .addInterceptor { chain ->
            val request: Request =
                chain
                    .request()
                    .newBuilder()
                    .addHeader("x-rapidapi-key", ApiConstants.rapidapiKey)
                    .addHeader("x-rapidapi-host", ApiConstants.rapidapiHost)
                    .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.nutritionAnalysisApiBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}