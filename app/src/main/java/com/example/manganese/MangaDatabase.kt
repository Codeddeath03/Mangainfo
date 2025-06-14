package com.example.manganese

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.manganese.components.Converters
import com.example.manganese.database.entities.Anime
import com.example.manganese.database.entities.Manga

@Database(entities = [Manga::class, Anime::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class MangaDatabase : RoomDatabase() {

    abstract fun mangaDao(): MangaDao

    companion object {
        @Volatile
        private var INSTANCE: MangaDatabase? = null

        fun getDatabase(context: Context): MangaDatabase {if (INSTANCE == null) {
            synchronized(MangaDatabase::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = databaseBuilder(
                        context.applicationContext,
                        MangaDatabase::class.java,
                        "manga_database"
                    ).createFromAsset("crazy_schema_manga.db")
                        .build() // Don't forget to build!
                }
            }
        }
            return INSTANCE!!
        }
    }
}