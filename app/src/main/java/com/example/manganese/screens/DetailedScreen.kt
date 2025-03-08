package com.example.manganese.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.manganese.MangaDao
import com.example.manganese.R
import com.example.manganese.components.ReadListButton
import com.example.manganese.components.UserViewModel
import com.example.manganese.components.WatchlistButton
import com.example.manganese.database.entities.Anime
import com.example.manganese.database.entities.Manga
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



@Composable
fun ShortTopAppBar(onBackAction:()-> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
           ,
        contentAlignment = Alignment.CenterStart
    ) {
        IconButton(onClick = { onBackAction()}) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
            )
        }
    }
}


@Composable
fun DetailScreen(
    Id: Int, mangaDao: MangaDao,
    type: String?,
    onBackAction: () -> Unit,
    viewModel: UserViewModel,
) {
    Scaffold(
        topBar = { ShortTopAppBar { onBackAction() }}
    ){  innerPadding ->
        val padding = Modifier.padding(innerPadding)
        val Details = remember { mutableStateOf<Any?>(null) }
        if( type == "anime") {
            LaunchedEffect(Unit) {
                val Detail = withContext(Dispatchers.IO) {
                    Log.d("AnimeListScreen", Thread.currentThread().name)
                    mangaDao.getAnimeEntryById(Id)
                }
                Details.value = Detail
            }
        }
        else{
            LaunchedEffect(Unit) {
                val Detail = withContext(Dispatchers.IO) {
                    Log.d("MangaListScreen", Thread.currentThread().name)
                    mangaDao.getMangaEntryById(Id)
                }
                Details.value = Detail
            }
        }
        Column(
            modifier = padding
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (Details.value != null) {
                when (val item = Details.value){
                    is Manga -> {

                        Log.d("MangaDetailScreen","chirkoot")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ){

                            //Image Box
                            Box(
                                modifier = Modifier
                                    .size(height = 300.dp, width = 250.dp)
                                    .border(1.dp, Color.Gray)
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                val painter = rememberAsyncImagePainter(item.mainPictureMedium)
                                val state by painter.state.collectAsState()

                                when (state) {
                                    is AsyncImagePainter.State.Loading -> {
                                        CircularProgressIndicator(
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    }

                                    is AsyncImagePainter.State.Success -> {
                                        Image(
                                            painter = painter,
                                            contentDescription = "Manga Image",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    else -> {
                                        Image(
                                            painter = painterResource(id = R.drawable.no_signal),
                                            contentDescription = "Error loading image",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }


                        Spacer(modifier = Modifier.height(16.dp))

                        // Manga Title and Genre
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ){
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.Red,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                ReadListButton(item.id,item.title,viewModel)
                            }


                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Genre: ${item.genres!!.joinToString(", ")}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                            )

                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Manga Synopsis
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = item.synopsis.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                        }
                    }

                    is Anime -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement =  Arrangement.Center
                        ){
                            // Anime Image Box
                            Box(
                                modifier = Modifier
                                    .size(height = 300.dp, width = 250.dp)
                                    .border(1.dp, Color.Gray)
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                val painter = rememberAsyncImagePainter(item.mainPictureMedium)
                                val state by painter.state.collectAsState()

                                when (state) {
                                    is AsyncImagePainter.State.Loading -> {
                                        CircularProgressIndicator(
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    }

                                    is AsyncImagePainter.State.Success -> {
                                        Image(
                                            painter = painter,
                                            contentDescription = "Manga Image",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    else -> {
                                        Image(
                                            painter = painterResource(id = R.drawable.no_signal),
                                            contentDescription = "Error loading image",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }


                        Spacer(modifier = Modifier.height(16.dp))

                        // Anime Title and Genre
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                text = item.title,
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.Red,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                                WatchlistButton(item.id,item.title,viewModel)
                            }


                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Genre: ${item.genres!!.joinToString(", ")}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                            )

                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Anime Synopsis
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = item.synopsis.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                        }
                    }
                }
//                Box(modifier = Modifier.clickable {onBackAction()}
//                    .padding(4.dp)
//                    .background(shape= RoundedCornerShape(12.dp),color=Color.Transparent)
//                    .fillMaxWidth(),
//                    contentAlignment = Alignment.TopStart
//                ){
//                    Icon(
//                        painter = painterResource(id=R.drawable.arrow_back),
//                        contentDescription = "Back Button"
//                    )
//                }

            }else {

                CircularProgressIndicator() // Loading indicator
            }
        }
    }

}
@Preview
@Composable
fun MangaDetailScreen() {
    val manga = Manga(
        id = 1,
        title = "Attack on Titan",
        mediaType = "TV Series",
        mean = 8.75,
        numScoringUsers = 500000,
        status = "Completed",
        numVolumes = 34,
        numChapters = 139,
        startDate = "2009-09-09",
        endDate = "2021-04-09",
        numListUsers = 2000000,
        popularity = 5,
        numFavorites = 800000,
        rank = 1,
        genres = listOf("Action", "Drama", "Fantasy"),
        authors = "Hajime Isayama",
        synopsis = """
        In a world where humanity is on the brink of extinction due to giant humanoid creatures known as Titans, 
        the story follows Eren Yeager and his companions as they fight for survival. Eren's quest for revenge against 
        the Titans leads him to uncover dark secrets about their origin and the world they live in. Filled with intense 
        action, deep political intrigue, and shocking twists, this manga explores themes of freedom, humanity, and sacrifice.
    """.trimIndent(),
        nsfw = "No",
        createdAt = "2009-09-09T00:00:00",
        updatedAt = "2021-04-09T00:00:00",
        mainPictureMedium = "https://example.com/attack_on_titan_medium.jpg",
        mainPictureLarge = "https://example.com/attack_on_titan_large.jpg",
        alternativeTitlesEn = "Shingeki no Kyojin",
        alternativeTitlesJa = "進撃の巨人",
        alternativeTitlesSynonyms = "The Assault of the Giants, Titans' Assault"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        if (manga != null) {

            // Manga Image
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier=Modifier
                        .background(Color.Black)
                        .fillMaxWidth(.1f)
                        .height(1.dp)
                )
                // Manga Image Box
                Box(
                    modifier = Modifier
                        .size(150.dp, height = 200.dp)
                        .border(1.dp, Color.Gray)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val painter = rememberAsyncImagePainter(manga.mainPictureMedium)
                    val state by painter.state.collectAsState()

                    when (state) {
                        is AsyncImagePainter.State.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                        is AsyncImagePainter.State.Success -> {
                            Image(
                                painter = painter,
                                contentDescription = "Manga Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        else -> {
                            Image(
                                painter = painterResource(id = R.drawable.no_signal),
                                contentDescription = "Error loading image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
                Box(
                    modifier=Modifier
                        .background(Color.Black)
                        .fillMaxWidth()
                        .height(1.dp)
                )
                //remaining line
                Column {

                    Text(
                        text = manga.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Red,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = manga.genres.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                    )
                }

            }


            Spacer(modifier = Modifier.height(16.dp))

            // Manga Title and Genre
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = manga.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Red,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = manga.genres.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Manga Synopsis
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(1.dp, Color.Gray)
                    .padding(8.dp)
            ) {
                Text(
                    text = manga.synopsis.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        }
        else {
            CircularProgressIndicator() // Loading indicator
        }
    }
}
