package com.example.manganese.components

import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import com.example.manganese.R
import com.example.manganese.database.Resources
import com.example.manganese.database.entities.AnimeSummary

@Composable
fun WatchlistButton(
    animeId: Int,
    animeTitle: String,
    viewModel: UserViewModel
) {
    // Directly collect the list of AnimeSummary (no Resources wrapper)
    val watchlistSummaries by viewModel.watchlistSummaries.collectAsState()

    // Check if this anime is already in the watchlist
    val isInWatchlist = watchlistSummaries.any { it.id == animeId }

    // Choose correct icon
    val icon = if (isInWatchlist) {
        painterResource(id = R.drawable.bookmark_filled)  // anime is in watchlist
    } else {
        painterResource(id = R.drawable.bookmark_add)  // anime is not in watchlist
    }

    IconButton(
        onClick = {
            if (isInWatchlist) {
                viewModel.removeAnimeFromWatchlist(animeId)
            } else {
                viewModel.addAnimeToWatchlist(animeId, animeTitle)
            }
        }
    ) {
        Icon(
            painter = icon,
            contentDescription = if (isInWatchlist) "Remove from Watchlist" else "Add to Watchlist"
        )
    }
}

@Composable
fun ReadListButton(
    mangaId: Int,
    mangaTitle: String,
    viewModel: UserViewModel
) {
    val readlistSummaries by viewModel.readlistSummaries.collectAsState()


    val isInReadlist = readlistSummaries.any { it.id == mangaId }

    // Choose correct icon
    val icon = if (isInReadlist) {
        painterResource(id = R.drawable.bookmark_filled)  // anime is in watchlist
    } else {
        painterResource(id = R.drawable.bookmark_add)  // anime is not in watchlist
    }

    IconButton(
        onClick = {
            if (isInReadlist) {
                viewModel.removeMangaFromReadlist(mangaId)
            } else {
                viewModel.addMangaToReadlist(mangaId, mangaTitle)
            }
        }
    ) {
        Icon(
            painter = icon,
            contentDescription = if (isInReadlist) "Remove from Readlist" else "Add to Readlist"
        )
    }
}

