package ru.mts.data.news.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mts.data.news.db.NewsLocalDataSource
import ru.mts.data.news.remote.NewsRemoteDataSource
import ru.mts.data.news.remote.NewsResponse
import ru.mts.data.news.remote.toNewsEntity
import ru.mts.data.news.repository.model.News
import ru.mts.data.news.repository.model.NewsMapper
import ru.mts.data.utils.Result
import ru.mts.data.utils.doOnError
import ru.mts.data.utils.doOnSuccess

class NewsRepository(
    private val newsLocalDataSource: NewsLocalDataSource,
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val mapper: NewsMapper
) {
    suspend fun getNews(isForceUpdate: Boolean = false): Flow<Result<List<News>, Throwable>> {
        return if (isForceUpdate) {
            Log.d("help", "|---- NewsRepository getRemoteNewsFirstly")
            getRemoteNewsFirstly()
        } else {
            Log.d("help", "|---- NewsRepository getNewsCacheFirstly")
            getNewsCacheFirstly()
        }
    }

    private suspend fun getNewsCacheFirstly(): Flow<Result<List<News>, Throwable>> {
        return flow {
            var areNewsFromDBEmpty = false
            newsLocalDataSource.getNews()
                .doOnError { error ->
                    Log.d(
                        "help",
                        "|---- [NewsRepository,getNewsCacheFirstly] local doOnError $error"
                    )
                }
                .doOnSuccess { news ->
                    Log.d("help", "|---- [NewsRepository,getNewsCacheFirstly] local doOnSuccess $news")
                    if (news.isNotEmpty()) {
                        areNewsFromDBEmpty = true
                        emit(Result.Success(news.map { mapper.map(it) }))
                    }
                }
            newsRemoteDataSource.getNews()
                .doOnError { error ->
                    if (areNewsFromDBEmpty) {
                        Log.d(
                            "help",
                            "|--------- [NewsRepository,getNewsCacheFirstly] remote doOnError areNewsFromDBEmpty $areNewsFromDBEmpty"
                        )
                        emit(Result.Error(error))
                    }
                }
                .doOnSuccess { news ->
                    refreshCache(news)
                    Log.d("help", "|--------- [NewsRepository,getNewsCacheFirstly] remote doOnSuccess $news")
                    emit(Result.Success(news.map { mapper.map(it) }))
                }
        }
    }

    private suspend fun getRemoteNewsFirstly(): Flow<Result<List<News>, Throwable>> {
        return flow {
            newsRemoteDataSource.getNews()
                .doOnError { error ->
                    Log.d("help", "|--------- [NewsRepository, getRemoteNewsFirstly] remote doOnError $error")
                    emit(Result.Error(error))
                }
                .doOnSuccess { news ->
                    Log.d("help", "|-- [NewsRepository, getRemoteNewsFirstly] remote doOnSuccess $news")
                    refreshCache(news)
                    emit(Result.Success(news.map { mapper.map(it) }))
                }
        }
    }

    private suspend fun refreshCache(news: List<NewsResponse>) {
        //newsLocalDataSource.deleteCacheFromDB()
        newsLocalDataSource.setNews(news.map { it.toNewsEntity() })
    }
}
