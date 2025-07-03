package com.example.manganese.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.manganese.database.entities.Anime
import com.example.manganese.database.entities.AnimeSummary
import com.example.manganese.database.entities.Manga
import com.example.manganese.database.entities.MangaSummary
import com.example.manganese.database.entities.animeX
import com.example.manganese.database.entities.mangaX
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class dbViewModel(private val dbRepo: dbRepository): ViewModel() {
    val  animes: StateFlow<List<AnimeSummary>>
        get() = dbRepo.animes
    val manga: StateFlow<List<MangaSummary>>
        get() = dbRepo.manga
    private val _animeDetail = MutableStateFlow<Anime?>(null)
    val animeDetail: StateFlow<Anime?> = _animeDetail
    private val _mangaDetail = MutableStateFlow<Manga?>(null)
    val mangaDetail: StateFlow<Manga?> = _mangaDetail

    private val _searchState = MutableStateFlow(false)
    val searchState = _searchState.asStateFlow()
    fun updateSearchQuery(query: String) {
        _searchState.value = query.isNotEmpty()
    }
    private val _mlist = MutableStateFlow<PagingData<MangaSummary>>(PagingData.empty())
    val mlist = _mlist.asStateFlow()

    private val _alist = MutableStateFlow<PagingData<AnimeSummary>>(PagingData.empty())
    val alist = _alist.asStateFlow()

    val Mangapager = Pager(PagingConfig(pageSize = 5, prefetchDistance = 20)) {
        dbRepo.loadallMangasPaged()
    }.flow.cachedIn(viewModelScope)

    val Animepager = Pager(PagingConfig(pageSize = 5, prefetchDistance = 20)) {
        dbRepo.loadallAnimesPaged()
    }.flow.cachedIn(viewModelScope)

    fun setAnimeDetailnull() {_animeDetail.value = null}
    fun setMangaDetailnull() {_mangaDetail.value = null}
    fun Mangasearch(query: String){
        viewModelScope.launch{
            Pager(PagingConfig(pageSize = 20)) {
                dbRepo.searchMangaPaged(query)
            }.flow.cachedIn(viewModelScope).collectLatest { pagingData ->
                _mlist.value = pagingData

            }
        }

    }
    fun Animesearch(query:String){
        viewModelScope.launch{
            Pager(PagingConfig(pageSize=5)){
                dbRepo.searchAnimePaged(query)
            }.flow.cachedIn(viewModelScope).collectLatest {
                pagingData ->
                _alist.value = pagingData
            }
        }
    }
    fun detailedAnimeSearch(id:Int){
        viewModelScope.launch {
           val anime = dbRepo.detailedAnimeSearch(id)
            _animeDetail.value = anime
        }
    }
    fun detailedMangaSearch(id:Int){
        viewModelScope.launch {
            val manga = dbRepo.detailedMangaSearch(id)
            _mangaDetail.value = manga
        }
    }

    init {
        viewModelScope.launch {
            dbRepo.getTrendingAnimes()
            dbRepo.getTrendingMangas()
        }
    }


}