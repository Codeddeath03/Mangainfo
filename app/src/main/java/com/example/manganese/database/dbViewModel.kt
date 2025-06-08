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

class dbViewModel(private val dbRepo: dbRepository): ViewModel() {
    val Mangapager = Pager(PagingConfig(pageSize = 20)) {
        dbRepo.loadallMangasPaged()
    }.flow.cachedIn(viewModelScope)

    val Animepager = Pager(PagingConfig(pageSize = 20)) {
        dbRepo.loadallAnimesPaged()
    }.flow.cachedIn(viewModelScope)

    fun Mangasearch(query: String): Flow<PagingData<MangaSummary>> {
        return Pager(PagingConfig(pageSize = 20)) {
            dbRepo.searchMangaPaged(query)
        }.flow.cachedIn(viewModelScope)
    }
    fun Animesearch(query: String): Flow<PagingData<AnimeSummary>> {
        return Pager(PagingConfig(pageSize = 20)) {
            dbRepo.searchAnimePaged(query)
        }.flow.cachedIn(viewModelScope)
    }

}