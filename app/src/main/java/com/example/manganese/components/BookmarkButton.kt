package com.example.manganese.components

import android.util.Log
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
    viewModel: UserViewModel? = null
) {
    if (viewModel == null) {
        // When viewModel is null (e.g., in previews), show disabled icon
        IconButton(onClick = {}, enabled = false) {
            Icon(
                painter = painterResource(id = R.drawable.watchlist_add),
                contentDescription = "Add to Watchlist (Disabled)"
            )
        }
        return
    }

    // Directly collect the list of AnimeSummary (no Resources wrapper)
    val watchlistSummaries by viewModel.watchlistSummaries.collectAsState()
    Log.d("WatchlistUI", "Current watchlist: $watchlistSummaries")
    // Check if this anime is already in the watchlist
    val isInWatchlist = watchlistSummaries.any { it.id == animeId }

    // Choose correct icon
    val icon = if (isInWatchlist) {
        painterResource(id = R.drawable.watchlist_check)  // anime is in watchlist
    } else {
        painterResource(id = R.drawable.watchlist_add)  // anime is not in watchlist
    }

    IconButton(
        onClick = {
            Log.d("watchlist button","clicked")
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
    viewModel: UserViewModel?= null
) {
    if (viewModel == null) {
        // When viewModel is null (e.g., in previews), show disabled icon
        IconButton(onClick = {}, enabled = false) {
            Icon(
                painter = painterResource(id = R.drawable.bookmark_add),
                contentDescription = "Add to Watchlist (Disabled)"
            )
        }
        return
    }
    val readlistSummaries by viewModel.readlistSummaries.collectAsState()
    Log.d("ReadlistUI", "Current readlist: $readlistSummaries")

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

