package com.example.manganese.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
@Entity(
    tableName = "anime",
    foreignKeys = [
        ForeignKey(
            entity = Manga::class,
            parentColumns = ["id"],
            childColumns = ["manga_id"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.NO_ACTION

            )
    ]
)
data class Anime(
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

    @ColumnInfo(name = "num_episodes")
    val numEpisodes: Int?,

    @ColumnInfo(name = "start_date")
    val startDate: String?,

    @ColumnInfo(name = "end_date")
    val endDate: String?,

    @ColumnInfo(name = "source")
    val source: String?,

    @ColumnInfo(name = "num_list_users")
    val numListUsers: Int?,

    @ColumnInfo(name = "popularity")
    val popularity: Int?,

    @ColumnInfo(name = "num_favorites")
    val numFavorites: Int?,

    @ColumnInfo(name = "rank")
    val rank: Int?,

    @ColumnInfo(name = "average_episode_duration")
    val averageEpisodeDuration: String?,

    @ColumnInfo(name = "rating")
    val rating: String?,

    @ColumnInfo(name = "start_season_year")
    val startSeasonYear: Int?,

    @ColumnInfo(name = "start_season_season")
    val startSeasonSeason: String?,

    @ColumnInfo(name = "broadcast_day_of_the_week")
    val broadcastDayOfTheWeek: String?,

    @ColumnInfo(name = "broadcast_start_time")
    val broadcastStartTime: String?,

    @ColumnInfo(name = "genres")
    val genres: List<String>?,

    @ColumnInfo(name = "studios")
    val studios: String?,

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
    val alternativeTitlesSynonyms: String?,

    @ColumnInfo(name = "manga_id")
    val mangaId: Int?
)

data class AnimeSummary(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "main_picture_medium") val mainPictureMedium: String?
)