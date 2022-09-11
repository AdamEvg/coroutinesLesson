package ru.mts.data.news.db

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mts.data.main.AppDatabase
import ru.mts.data.utils.Result
import ru.mts.data.utils.VoidResult
import ru.mts.data.utils.runOperationCatching

class NewsLocalDataSource(private val context: Context) {

    suspend fun getNews(): Result<List<NewsEntity>, Throwable> {
        return runOperationCatching {
            withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(context).newsDao().getAll() ?: emptyList()
            }
        }
    }

    suspend fun setNews(news: List<NewsEntity>): VoidResult<Throwable> {
        return runOperationCatching {
            withContext(Dispatchers.IO) {
                val newsDao = AppDatabase.getDatabase(context).newsDao()
                news.forEach { entity -> newsDao.insert(entity) }
            }
        }
    }

    suspend fun deleteCacheFromDB(): VoidResult<Throwable> {
        return runOperationCatching {
            withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(context).newsDao().delete()
            }
        }
    }
}
