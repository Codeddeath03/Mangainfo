package com.example.manganese.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.manganese.database.entities.AnimeSummary
import com.example.manganese.database.entities.MangaSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class dbViewModel(private val dbRepo: dbRepository): ViewModel() {

    val _searchState = MutableStateFlow(false)
    val searchState = _searchState.asStateFlow()

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



}