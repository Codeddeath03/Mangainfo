package com.example.manganese

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.manganese.database.entities.Manga

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

    // Add more specific queries based on your needs (e.g., by title, author, etc.)
    @Query("SELECT * FROM new_manga WHERE title LIKE :title")
    suspend fun getMangaEntriesByTitle(title: String): List<Manga>
}