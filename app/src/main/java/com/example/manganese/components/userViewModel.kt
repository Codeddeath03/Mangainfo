package com.example.manganese.components

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manganese.MangaDao
import com.example.manganese.database.Resources
import com.example.manganese.database.entities.AnimeSummary
import com.example.manganese.database.entities.MangaSummary
import com.example.manganese.database.firedb
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserViewModel(private val fireDB: firedb,private val mangaDao: MangaDao) : ViewModel() {
    val hasUser: Boolean
        get()= fireDB.hasUser()
    private val _nickname = MutableStateFlow<Resources<String>>(Resources.Loading())
    val nickname: StateFlow<Resources<String>> = _nickname.asStateFlow()

    private val _watchlist = MutableStateFlow<Resources<List<Int>>>(Resources.Loading())
    val watchlist: StateFlow<Resources<List<Int>>> = _watchlist

    private val _watchlistSummaries = MutableStateFlow<List<AnimeSummary>>(emptyList())
    val watchlistSummaries: StateFlow<List<AnimeSummary>> = _watchlistSummaries.asStateFlow()

    private val _signupState = MutableStateFlow<Resources<Unit>>(Resources.Loading())
    val signupState: StateFlow<Resources<Unit>> = _signupState

    private val _readlistSummaries = MutableStateFlow<List<MangaSummary>>(emptyList())
    val readlistSummaries: StateFlow<List<MangaSummary>> = _readlistSummaries.asStateFlow()



    init {
        if (hasUser){
            Log.d("UserViewModel", "User is logged in $hasUser")
            viewModelScope.launch {
                val nickname =fetchNickname()
                _nickname.value = nickname
            }
            observeUserLists()
        }
    }
    private fun observeUserLists() {
        fetchWatchlist(mangaDao)
       fetchReadlist(mangaDao)
    }

    suspend fun fetchNickname(): Resources<String> {
        return fireDB.getUserNickname().first { it !is Resources.Loading }
    }
    suspend fun fetchNicknamesuccess(): Resources<String> {
        return fireDB.getUserNickname().first { it is Resources.Success }
    }

//    private fun fetchWatchlist() {
//        viewModelScope.launch {
//            fireDB.getUserWatchlist().collect { _watchlist.value = it }
//        }
//    }
    fun fetchWatchlist(mangaDao: MangaDao) {
        viewModelScope.launch {
            fireDB.getUserWatchlist().collect { resource ->
                Log.d("WatchlistCollector", "Received: $resource")
                if (resource is Resources.Success) {
                    // Convert list of anime IDs into AnimeSummary list
                    val animeSummaries = resource.data?.mapNotNull { animeId ->
                        mangaDao.getAnimeSummarybyID(animeId) // This should return AnimeSummary?
                    } ?: emptyList()

                    // Set the result into the MutableStateFlow
                    _watchlistSummaries.value = animeSummaries
                } else {
                    Log.d("Watchlist", "failed")
                    _watchlistSummaries.value = emptyList() // Set empty if failed, you can modify this behavior
                }
            }
        }
    }
    fun fetchReadlist(mangaDao: MangaDao) {
        viewModelScope.launch {
            fireDB.getUserReadlist().collect { resource ->
                Log.d("ReadlistCollector", "Received: $resource")
                if (resource is Resources.Success) {
                    // Convert list of anime IDs into AnimeSummary list
                    val mangaSummaries = resource.data?.mapNotNull { mangaId ->
                        mangaDao.getMangaSummarybyID(mangaId) // This should return AnimeSummary?
                    } ?: emptyList()

                    // Set the result into the MutableStateFlow
                    _readlistSummaries.value = mangaSummaries
                } else {
                    Log.d("Readlist", "failed")
                    _readlistSummaries.value = emptyList() // Set empty if failed, you can modify this behavior
                }
            }
        }
    }
    fun addAnimeToWatchlist(animeId: Int, animeTitle: String) {
        viewModelScope.launch {
            val result = fireDB.addToWatchlist(animeId, animeTitle)
            if (result is Resources.Error) {
                Log.e("Watchlist", "Failed to add: $result")
            }
        }
    }
    fun removeAnimeFromWatchlist(animeId: Int) {
        viewModelScope.launch {
            val result = fireDB.removeFromWatchlist(animeId)
            if (result is Resources.Error) {
                Log.e("Watchlist", "Failed to remove: $result")
            }
        }
    }

    fun newUserRegister(email: String, password: String, nicknam: String,onComplete:() -> Unit){
        viewModelScope.launch {
            Log.d("register_user","$email $password $nicknam")
            _signupState.value = Resources.Loading()
            val result = fireDB.createUser(email, password, nicknam)
            if (result is Resources.Success) {
                val res = fetchNickname()
                _nickname.value = res
                if (res is Resources.Success) {
                    Log.d("UserRegister-nick","$nickname")
                    observeUserLists()
                    onComplete()
                } else {
                    // Handle error fetching nickname
                    Log.e("UserRegister", "Failed to fetch nickname: ${res.throwable}")
                }
//                Log.d("UserRegister-nick","$nickname")
//                onComplete()
            }
            _signupState.value = result
        }
    }




    fun addMangaToReadlist(mangaId: Int, mangaTitle: String) {
        viewModelScope.launch {
            val result = fireDB.addToReadlist(mangaId, mangaTitle)
            if (result is Resources.Error) {
                Log.e("Readlist", "Failed to add: $result")
            }
        }
    }
    fun removeMangaFromReadlist(mangaId: Int) {
        viewModelScope.launch {
            val result = fireDB.removeFromReadlist(mangaId)
            if (result is Resources.Error) {
                Log.e("Readlist", "Failed to remove: $result")
            }
        }
    }





}

