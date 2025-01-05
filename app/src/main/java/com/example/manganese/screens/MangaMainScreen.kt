package com.example.manganese.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.example.manganese.R
import com.example.manganese.database.entities.Manga
import com.example.manganese.database.entities.MangaSummary

@Composable
fun MangaMainScreen(mangaList: List<MangaSummary>,onMangaClick: (MangaId:Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Trending Section
        item {
            SectionTitle(title = "Trending")
        }
        item {
            Mangarow(modifier = Modifier, mangaList,
                onMangaClick)
        }

        // Recommendation Section
        item {
            SectionTitle(title = "Recommendation")
        }
        item {
            Mangarow(modifier = Modifier, mangaList,
                onMangaClick)
        }

        // Grid Section (Integrated into LazyColumn)
        item {
            SectionTitle(title = "All Manga")
        }
        items(
            mangaList.chunked(3)){
                rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { manga ->
                    MangaCard(
                        manga = manga,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        onMangaClick
                    )
                }
            }
        }

    }

}
@Composable
fun SectionTitle(title: String) {
    Text(text = title, modifier = Modifier.padding(16.dp))
}


@Composable
fun Mangarow(modifier: Modifier = Modifier, mangaList: List<MangaSummary>,onMangaClick: (MangaId:Int) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(mangaList) { manga ->
            MangaCard(manga,modifier = Modifier
                .width(120.dp) // Smaller card width for row
                .height(180.dp), // Smaller card height for row
                onMangaClick

            )
        }
    }
}



//@Composable
//fun MangaGrid(mangas: List<Manga>) {
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(3), // Adjust for 3 items in a row
//        modifier = Modifier.padding(8.dp),
//        contentPadding = PaddingValues(4.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        items(mangas) { mangas ->
//            MangaCard(mangas)
//        }
//    }
//}

@Composable
fun MangaCard(manga: MangaSummary,modifier: Modifier = Modifier,onMangaClick: (MangaId:Int) -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .clickable { onMangaClick(manga.id) },
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val painter = rememberAsyncImagePainter(
                model = manga.mainPictureMedium,
                contentScale = ContentScale.Crop,
            )
            val state by painter.state.collectAsState()
            when (state) {
                is AsyncImagePainter.State.Empty,
                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator()
                }

                is AsyncImagePainter.State.Success -> {
                    Image(
                        painter = painter,
                        contentDescription = manga.title,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                is AsyncImagePainter.State.Error -> {
                    // Show some error UI.
                    Image(

                        painter = painterResource(id = R.drawable.no_signal),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Error loading image",
                        modifier = Modifier.fillMaxSize(),

                        )
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 300f
                        )
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = manga.title,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
