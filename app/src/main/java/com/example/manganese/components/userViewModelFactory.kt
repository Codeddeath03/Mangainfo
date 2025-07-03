package com.example.manganese.components
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.manganese.MangaDao
import com.example.manganese.components.UserViewModel
import com.example.manganese.database.FireDBRepository
import com.example.manganese.database.dbRepository

class UserViewModelFactory(private val fireDB: FireDBRepository, private val mangaDao: MangaDao,private val dbRepository: dbRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(fireDB,mangaDao,dbRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
