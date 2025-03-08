package com.example.manganese

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.manganese.database.entities.Anime
import com.example.manganese.database.entities.AnimeSummary
import com.example.manganese.database.entities.Manga
import com.example.manganese.database.entities.MangaSummary

@Dao
interface MangaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMangaEntry(mangaEntry: Manga)

    @Delete
    suspend fun deleteMangaEntry(mangaEntry: Manga)

    @Update
    suspend fun updateMangaEntry(mangaEntry: Manga)

    @Query("SELECT * FROM new_manga LIMIT :limit OFFSET :offset")
    suspend fun getAllMangaEntries(limit: Int, offset: Int): List<Manga>

    @Query("SELECT * FROM new_manga")
    suspend fun getAllMangas(): List<Manga>

    @Query("SELECT id, title, main_picture_medium FROM new_manga")
    suspend fun getMangaSummaries(): List<MangaSummary>

    @Query("SELECT id, title, main_picture_medium FROM new_manga WHERE id = :id")
    suspend fun getMangaSummarybyID(id: Int): MangaSummary

    @Query("SELECT * FROM new_manga WHERE id = :id")
    suspend fun getMangaEntryById(id: Int): Manga

    @Query("SELECT id, title, main_picture_medium FROM new_anime")
    suspend fun getAnimeSummaries(): List<AnimeSummary>

    @Query("SELECT id, title, main_picture_medium FROM new_anime WHERE id = :id")
    suspend fun getAnimeSummarybyID(id: Int): AnimeSummary

    @Query("SELECT * FROM new_anime WHERE id = :id")
    suspend fun getAnimeEntryById(id: Int): Anime

    @Query("SELECT * FROM new_manga WHERE title LIKE :title")
    suspend fun getMangaEntriesByTitle(title: String): List<Manga>
}