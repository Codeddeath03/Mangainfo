package com.example.manganese.database

import androidx.paging.PagingSource
import com.example.manganese.MangaDao
import com.example.manganese.database.entities.AnimeSummary
import com.example.manganese.database.entities.MangaSummary

class dbRepository(private val dbDAO: MangaDao){



    fun loadallMangasPaged(): PagingSource<Int, MangaSummary> = dbDAO.getallMangaSummariesPaged()

    fun loadallAnimesPaged(): PagingSource<Int, AnimeSummary> = dbDAO.getallAnimeSummariesPaged()

    fun searchMangaPaged(title: String): PagingSource<Int, MangaSummary> = dbDAO.searchMangaEntriesByTitle(title)

    fun searchAnimePaged(title: String): PagingSource<Int, AnimeSummary> = dbDAO.searchAnimeEntriesByTitle(title)

}