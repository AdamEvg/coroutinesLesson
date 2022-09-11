package ru.mts.data.news.repository.model

import ru.mts.data.news.db.NewsEntity
import ru.mts.data.news.remote.NewsResponse

class NewsMapper {

    fun map(entity: NewsEntity) = News(
        id = entity.id,
        name = entity.name,
        description = entity.description
    )

    fun map(response: NewsResponse) = News(
        id = response.id,
        name = response.name,
        description = response.description
    )
}