package com.example.manganese

import androidx.paging.PagingSource
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeEntry(animeEntry: Anime)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMangaList(mangaList: List<Manga>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeList(animeList: List<Anime>)

    @Delete
    suspend fun deleteMangaEntry(mangaEntry: Manga)

    @Update
    suspend fun updateMangaEntry(mangaEntry: Manga)

    @Query("SELECT * FROM manga LIMIT :limit OFFSET :offset")
    suspend fun getAllMangaEntries(limit: Int, offset: Int): List<Manga>

    @Query("SELECT * FROM manga")
    suspend fun getAllMangas(): List<Manga>

    @Query("SELECT id, title, main_picture_medium FROM manga")
    suspend fun getMangaSummaries(): List<MangaSummary>

    @Query("SELECT id, title, main_picture_medium FROM manga")
    fun getallMangaSummariesPaged(): PagingSource<Int, MangaSummary>

    @Query("SELECT id, title, main_picture_medium FROM manga WHERE id = :id")
    suspend fun getMangaSummarybyID(id: Int): MangaSummary?

    @Query("SELECT * FROM manga WHERE id = :id")
    suspend fun getMangaEntryById(id: Int): Manga


    @Query("SELECT id, title, main_picture_medium FROM anime")
    suspend fun getAnimeSummaries(): List<AnimeSummary>

    @Query("SELECT id, title, main_picture_medium FROM anime")
    fun getallAnimeSummariesPaged(): PagingSource<Int, AnimeSummary>

    @Query("SELECT id, title, main_picture_medium FROM anime WHERE id = :id")
    suspend fun getAnimeSummarybyID(id: Int): AnimeSummary

    @Query("SELECT * FROM anime WHERE id = :id")
    suspend fun getAnimeEntryById(id: Int): Anime

    @Query("SELECT * FROM anime WHERE id = :id")
    suspend fun getAnimeEntriesById(id: Int): List<AnimeSummary>

    @Query("SELECT * FROM manga WHERE title LIKE :title")
    suspend fun getMangaEntriesByTitle(title: String): List<Manga>

    @Query("SELECT id, title, main_picture_medium FROM manga WHERE title  LIKE '%' || :title || '%'")
    fun searchMangaEntriesByTitle(title: String): PagingSource<Int, MangaSummary>

    @Query("SELECT id, title, main_picture_medium FROM anime WHERE title  LIKE '%' || :title || '%'")
    fun searchAnimeEntriesByTitle(title: String): PagingSource<Int, AnimeSummary>

    @Query("SELECT id, title, main_picture_medium FROM anime WHERE id IN (:ids)")
    suspend fun getAnimeSummariesByIds(ids: List<Int>): List<AnimeSummary>
}