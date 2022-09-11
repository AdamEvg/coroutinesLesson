package ru.mts.data.news.remote

import com.google.gson.annotations.SerializedName
import ru.mts.data.news.db.NewsEntity

data class NewsResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
)

internal fun NewsResponse.toNewsEntity() = NewsEntity(
    id = id,
    name = name,
    description = description
)