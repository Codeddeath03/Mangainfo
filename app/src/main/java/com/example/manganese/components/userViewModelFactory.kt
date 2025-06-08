package com.example.manganese.components
import com.example.manganese.database.firedb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.manganese.MangaDao
import com.example.manganese.components.UserViewModel

class UserViewModelFactory(private val fireDB: firedb, private val mangaDao: MangaDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(fireDB,mangaDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
