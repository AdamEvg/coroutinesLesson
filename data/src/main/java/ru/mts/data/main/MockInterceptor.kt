package ru.mts.data.main

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.asResponseBody
import okhttp3.mockwebserver.MockResponse
import ru.mts.data.news.remote.NewsApiService
import ru.mts.data.utils.ResourcesReader

class MockInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val uri = chain.request().url.toUri().toString()
        val body =
            MockResponse().setBodyFromFile(
                when {
                    uri.endsWith(NewsApiService.GET_NEWS_METHOD) -> "assets/news.json"
                    else -> ""
                }

            ).getBody()
                ?.asResponseBody("application/json".toMediaTypeOrNull())

        return chain.proceed(chain.request())
            .newBuilder()
            .code(200)
            .protocol(Protocol.HTTP_2)
            .body(
                body
            )
            .addHeader("content-type", "application/json")
            .build()
    }

    private fun MockResponse.setBodyFromFile(filName: String): MockResponse {
        val text = ResourcesReader.readText(filName)
        setBody(text)
        return this
    }
}

