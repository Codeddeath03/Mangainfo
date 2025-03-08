package com.example.manganese.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.manganese.components.UserViewModel



@Composable
fun watchListScreen(viewModel: UserViewModel,onAnimeClick: (animeId:Int) -> Unit) {
    val wl by viewModel.watchlistSummaries.collectAsState()
    Column {
        SectionTitle("Watchlist")
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(wl.chunked(3)){
                    rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { manga ->
                        AnimeCard(manga,modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                            onAnimeClick

                        )

                    }
                }
            }
        }
    }

}
@Composable
fun readListScreen(viewModel: UserViewModel,onMangaClick: (MangaId:Int) -> Unit) {
    val wl by viewModel.readlistSummaries.collectAsState()
    Column {
        SectionTitle("Readlist")
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(wl.chunked(3)){
                    rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { manga ->
                        MangaCard(manga,modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                            onMangaClick

                        )

                    }
                }
            }
        }
    }

}