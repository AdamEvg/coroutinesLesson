package ru.mts.data.news.remote

import retrofit2.http.GET
import retrofit2.http.Headers

interface NewsApiService {

    @GET(GET_NEWS_METHOD)
    @Headers("Content-Type:application/json; charset=utf-8;")
    suspend fun getNews(): List<NewsResponse>

    companion object {
        private const val SERVICE_NAME = "my-service-news"
        private const val API_VERSION = "v1"
        private const val METHOD_NAME = "news"
        const val GET_NEWS_METHOD = "$SERVICE_NAME/$API_VERSION/$METHOD_NAME"
    }
}
