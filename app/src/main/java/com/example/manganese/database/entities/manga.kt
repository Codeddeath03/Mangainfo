package com.example.manganese.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.manganese.components.Converters

@Entity(tableName = "manga")
data class Manga(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "media_type")
    val mediaType: String?,

    @ColumnInfo(name = "mean")
    val mean: Double?,

    @ColumnInfo(name = "num_scoring_users")
    val numScoringUsers: Int?,

    @ColumnInfo(name = "status")
    val status: String?,

    @ColumnInfo(name = "num_volumes")
    val numVolumes: Int?,

    @ColumnInfo(name = "num_chapters")
    val numChapters: Int?,

    @ColumnInfo(name = "start_date")
    val startDate: String?,

    @ColumnInfo(name = "end_date")
    val endDate: String?,

    @ColumnInfo(name = "num_list_users")
    val numListUsers: Int?,

    @ColumnInfo(name = "popularity")
    val popularity: Int?,

    @ColumnInfo(name = "num_favorites")
    val numFavorites: Int?,

    @ColumnInfo(name = "rank")
    val rank: Int?,

    @ColumnInfo(name = "genres")
    val genres: List<String>?,

    @ColumnInfo(name = "authors")
    val authors: String?,

    @ColumnInfo(name = "synopsis")
    val synopsis: String?,

    @ColumnInfo(name = "nsfw")
    val nsfw: String?,

    @ColumnInfo(name = "created_at")
    val createdAt: String?,

    @ColumnInfo(name = "updated_at")
    val updatedAt: String?,

    @ColumnInfo(name = "main_picture_medium")
    val mainPictureMedium: String?,

    @ColumnInfo(name = "main_picture_large")
    val mainPictureLarge: String?,

    @ColumnInfo(name = "alternative_titles_en")
    val alternativeTitlesEn: String?,

    @ColumnInfo(name = "alternative_titles_ja")
    val alternativeTitlesJa: String?,

    @ColumnInfo(name = "alternative_titles_synonyms")
    val alternativeTitlesSynonyms: String?
)



data class MangaSummary(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "main_picture_medium") val mainPictureMedium: String?
)
fun Manga.toSummary(): MangaSummary{
    return MangaSummary(
        id = this.id,
        title = this.title,
        mainPictureMedium = this.mainPictureMedium
    )
}