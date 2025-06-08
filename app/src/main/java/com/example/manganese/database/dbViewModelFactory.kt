package com.example.manganese.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.manganese.components.UserViewModel

class DbViewModelFactory(private val dbRepo: dbRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(dbViewModel::class.java)) {
            return dbViewModel(dbRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
