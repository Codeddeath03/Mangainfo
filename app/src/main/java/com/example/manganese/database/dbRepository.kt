package com.example.manganese.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import com.example.manganese.MangaDao
import com.example.manganese.api.bkendService
import com.example.manganese.database.entities.Anime
import com.example.manganese.database.entities.AnimeSummary
import com.example.manganese.database.entities.Manga
import com.example.manganese.database.entities.MangaSummary
import com.example.manganese.database.entities.animeX
import com.example.manganese.database.entities.mangaX
import com.example.manganese.database.entities.toSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class dbRepository(private val dbDAO: MangaDao,private val bkendService: bkendService){
    private val _manga = MutableStateFlow<List<MangaSummary>>(emptyList())
    val  manga: StateFlow<List<MangaSummary>> = _manga.asStateFlow()

    private val _anime = MutableStateFlow<List<AnimeSummary>>(emptyList())
    val  animes: StateFlow<List<AnimeSummary>> = _anime.asStateFlow()


    suspend fun getTrendingAnimes() {
        try {
            val result = bkendService.getTrendingAnimes()
            result.body()?.let{ body->
                _anime.value = body.data.map { it.toSummary() }
                if (body.db_insert){
                    Log.d("DBREPO","Inserting Animes: ${body.insertion_list}")
                 //   dbDAO.insertAnimeList(body.insertion_list)
                }
            }
        }
        catch (e:Exception){
            Log.e("NetworkCall", "Failed to connect", e)
        }
    }

    suspend fun getTrendingMangas() {
        try {
            val result = bkendService.getTrendingMangas()
            result.body()?.let{ body->
                _manga.value = body.data.map { it.toSummary()}
                if (body.db_insert){
                    Log.d("DBREPO","Inserting Mangas: ${body.insertion_list}")
                 //   dbDAO.insertMangaList(body.insertion_list)
                }
            }
        }
        catch (e:Exception){
            Log.e("NetworkCall", "Failed to connect", e)
        }
    }

    fun loadallMangasPaged(): PagingSource<Int, MangaSummary> = dbDAO.getallMangaSummariesPaged()

    fun loadallAnimesPaged(): PagingSource<Int, AnimeSummary> = dbDAO.getallAnimeSummariesPaged()

    fun searchMangaPaged(title: String): PagingSource<Int, MangaSummary> = dbDAO.searchMangaEntriesByTitle(title)

    fun searchAnimePaged(title: String): PagingSource<Int, AnimeSummary> = dbDAO.searchAnimeEntriesByTitle(title)

}
