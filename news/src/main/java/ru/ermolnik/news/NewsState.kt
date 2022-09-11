package ru.ermolnik.news

import ru.mts.data.news.repository.model.News

sealed class NewsState {

    object Loading : NewsState()
    data class Error(val throwable: Throwable) : NewsState()
    data class Content(val news: List<News>) : NewsState()
}