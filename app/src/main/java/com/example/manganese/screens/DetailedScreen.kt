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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
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

//
//@Composable
//fun DetailScreen(
//    Id: Int, mangaDao: MangaDao,
//    type: String?,
//    onBackAction: () -> Unit,
//    viewModel: UserViewModel,
//) {
//    Scaffold(
//        topBar = { ShortTopAppBar { onBackAction() }}
//    ){  innerPadding ->
//        val padding = Modifier.padding(innerPadding)
//        val Details = remember { mutableStateOf<Any?>(null) }
//        if( type == "anime") {
//            LaunchedEffect(Unit) {
//                val Detail = withContext(Dispatchers.IO) {
//                    Log.d("AnimeListScreen", Thread.currentThread().name)
//                    mangaDao.getAnimeEntryById(Id)
//                }
//                Details.value = Detail
//            }
//        }
//        else{
//            LaunchedEffect(Unit) {
//                val Detail = withContext(Dispatchers.IO) {
//                    Log.d("MangaListScreen", Thread.currentThread().name)
//                    mangaDao.getMangaEntryById(Id)
//                }
//                Details.value = Detail
//            }
//        }
//        Column(
//            modifier = padding
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            if (Details.value != null) {
//                when (val item = Details.value){
//                    is Manga -> {
//
//                        Log.d("MangaDetailScreen","chirkoot")
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.Center
//                        ){
//
//                            //Image Box
//                            Box(
//                                modifier = Modifier
//                                    .size(height = 300.dp, width = 250.dp)
//                                    .border(1.dp, Color.Gray)
//                                    .padding(4.dp),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                val painter = rememberAsyncImagePainter(item.mainPictureMedium)
//                                val state by painter.state.collectAsState()
//
//                                when (state) {
//                                    is AsyncImagePainter.State.Loading -> {
//                                        CircularProgressIndicator(
//                                            modifier = Modifier.align(Alignment.Center)
//                                        )
//                                    }
//
//                                    is AsyncImagePainter.State.Success -> {
//                                        Image(
//                                            painter = painter,
//                                            contentDescription = "Manga Image",
//                                            contentScale = ContentScale.Crop,
//                                            modifier = Modifier.fillMaxSize()
//                                        )
//                                    }
//
//                                    else -> {
//                                        Image(
//                                            painter = painterResource(id = R.drawable.no_signal),
//                                            contentDescription = "Error loading image",
//                                            contentScale = ContentScale.Crop,
//                                            modifier = Modifier.fillMaxSize()
//                                        )
//                                    }
//                                }
//                            }
//                        }
//
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        // Manga Title and Genre
//                        Column(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                            ){
//                                Text(
//                                    text = item.title,
//                                    style = MaterialTheme.typography.headlineSmall,
//                                    color = Color.Red,
//                                    maxLines = 1,
//                                    overflow = TextOverflow.Ellipsis
//                                )
//                                ReadListButton(item.id,item.title,viewModel)
//                            }
//
//
//                            Spacer(modifier = Modifier.height(8.dp))
//                            Text(
//                                text = "Genre: ${item.genres!!.joinToString(", ")}",
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = Color.Gray,
//                            )
//
//                        }
//
//                        Spacer(modifier = Modifier.height(24.dp))
//
//                        // Manga Synopsis
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(200.dp)
//                                .border(1.dp, Color.Gray)
//                                .padding(8.dp)
//                        ) {
//                            Text(
//                                text = item.synopsis.toString(),
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = Color.Black
//                            )
//                        }
//                    }
//
//                    is Anime -> {
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement =  Arrangement.Center
//                        ){
//                            // Anime Image Box
//                            Box(
//                                modifier = Modifier
//                                    .size(height = 300.dp, width = 250.dp)
//                                    .border(1.dp, Color.Gray)
//                                    .padding(4.dp),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                val painter = rememberAsyncImagePainter(item.mainPictureMedium)
//                                val state by painter.state.collectAsState()
//
//                                when (state) {
//                                    is AsyncImagePainter.State.Loading -> {
//                                        CircularProgressIndicator(
//                                            modifier = Modifier.align(Alignment.Center)
//                                        )
//                                    }
//
//                                    is AsyncImagePainter.State.Success -> {
//                                        Image(
//                                            painter = painter,
//                                            contentDescription = "Manga Image",
//                                            contentScale = ContentScale.Crop,
//                                            modifier = Modifier.fillMaxSize()
//                                        )
//                                    }
//
//                                    else -> {
//                                        Image(
//                                            painter = painterResource(id = R.drawable.no_signal),
//                                            contentDescription = "Error loading image",
//                                            contentScale = ContentScale.Crop,
//                                            modifier = Modifier.fillMaxSize()
//                                        )
//                                    }
//                                }
//                            }
//                        }
//
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        // Anime Title and Genre
//                        Column(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Text(
//                                text = item.title,
//                                style = MaterialTheme.typography.headlineSmall,
//                                color = Color.Red,
//                                maxLines = 1,
//                                overflow = TextOverflow.Ellipsis
//                            )
//                                WatchlistButton(item.id,item.title,viewModel)
//                            }
//
//
//                            Spacer(modifier = Modifier.height(8.dp))
//
//                            Text(
//                                text = "Genre: ${item.genres!!.joinToString(", ")}",
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = Color.Gray,
//                            )
//
//                        }
//
//                        Spacer(modifier = Modifier.height(24.dp))
//
//                        // Anime Synopsis
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(200.dp)
//                                .border(1.dp, Color.Gray)
//                                .padding(8.dp)
//                        ) {
//                            Text(
//                                text = item.synopsis.toString(),
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = Color.Black
//                            )
//                        }
//                    }
//                }
////                Box(modifier = Modifier.clickable {onBackAction()}
////                    .padding(4.dp)
////                    .background(shape= RoundedCornerShape(12.dp),color=Color.Transparent)
////                    .fillMaxWidth(),
////                    contentAlignment = Alignment.TopStart
////                ){
////                    Icon(
////                        painter = painterResource(id=R.drawable.arrow_back),
////                        contentDescription = "Back Button"
////                    )
////                }
//
//            }else {
//
//                CircularProgressIndicator() // Loading indicator
//            }
//        }
//    }
//
//}
//@Preview
//@Composable
//fun MangaDetailScreen() {
//    val manga = Manga(
//        id = 1,
//        title = "Attack on Titan",
//        mediaType = "TV Series",
//        mean = 8.75,
//        numScoringUsers = 500000,
//        status = "Completed",
//        numVolumes = 34,
//        numChapters = 139,
//        startDate = "2009-09-09",
//        endDate = "2021-04-09",
//        numListUsers = 2000000,
//        popularity = 5,
//        numFavorites = 800000,
//        rank = 1,
//        genres = listOf("Action", "Drama", "Fantasy"),
//        authors = "Hajime Isayama",
//        synopsis = """
//        In a world where humanity is on the brink of extinction due to giant humanoid creatures known as Titans,
//        the story follows Eren Yeager and his companions as they fight for survival. Eren's quest for revenge against
//        the Titans leads him to uncover dark secrets about their origin and the world they live in. Filled with intense
//        action, deep political intrigue, and shocking twists, this manga explores themes of freedom, humanity, and sacrifice.
//    """.trimIndent(),
//        nsfw = "No",
//        createdAt = "2009-09-09T00:00:00",
//        updatedAt = "2021-04-09T00:00:00",
//        mainPictureMedium = "https://example.com/attack_on_titan_medium.jpg",
//        mainPictureLarge = "https://example.com/attack_on_titan_large.jpg",
//        alternativeTitlesEn = "Shingeki no Kyojin",
//        alternativeTitlesJa = "進撃の巨人",
//        alternativeTitlesSynonyms = "The Assault of the Giants, Titans' Assault"
//    )
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//
//        if (manga != null) {
//
//            // Manga Image
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ){
//                Box(
//                    modifier=Modifier
//                        .background(Color.Black)
//                        .fillMaxWidth(.1f)
//                        .height(1.dp)
//                )
//                // Manga Image Box
//                Box(
//                    modifier = Modifier
//                        .size(150.dp, height = 200.dp)
//                        .border(1.dp, Color.Gray)
//                        .padding(4.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    val painter = rememberAsyncImagePainter(manga.mainPictureMedium)
//                    val state by painter.state.collectAsState()
//
//                    when (state) {
//                        is AsyncImagePainter.State.Loading -> {
//                            CircularProgressIndicator(
//                                modifier = Modifier.align(Alignment.Center)
//                            )
//                        }
//
//                        is AsyncImagePainter.State.Success -> {
//                            Image(
//                                painter = painter,
//                                contentDescription = "Manga Image",
//                                contentScale = ContentScale.Crop,
//                                modifier = Modifier.fillMaxSize()
//                            )
//                        }
//
//                        else -> {
//                            Image(
//                                painter = painterResource(id = R.drawable.no_signal),
//                                contentDescription = "Error loading image",
//                                contentScale = ContentScale.Crop,
//                                modifier = Modifier.fillMaxSize()
//                            )
//                        }
//                    }
//                }
//                Box(
//                    modifier=Modifier
//                        .background(Color.Black)
//                        .fillMaxWidth()
//                        .height(1.dp)
//                )
//                //remaining line
//                Column {
//
//                    Text(
//                        text = manga.title,
//                        style = MaterialTheme.typography.headlineSmall,
//                        color = Color.Red,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text(
//                        text = manga.genres.toString(),
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = Color.Gray,
//                    )
//                }
//
//            }
//
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Manga Title and Genre
//            Column(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(
//                    text = manga.title,
//                    style = MaterialTheme.typography.headlineSmall,
//                    color = Color.Red,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Text(
//                    text = manga.genres.toString(),
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = Color.Gray,
//                )
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // Manga Synopsis
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp)
//                    .border(1.dp, Color.Gray)
//                    .padding(8.dp)
//            ) {
//                Text(
//                    text = manga.synopsis.toString(),
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = Color.Black
//                )
//            }
//        }
//        else {
//            CircularProgressIndicator() // Loading indicator
//        }
//    }
//}

@Composable
fun MediaDetailContent(item: Any, viewModel: UserViewModel? = null,onBackAction: () -> Unit) {
    Scaffold(
        topBar = { ShortTopAppBar { onBackAction() }}
    ){  innerPadding ->
        val padding = Modifier.padding(innerPadding)
        Log.d("detailScreen","All goof!")
    val (id, title, imageUrl, genres, mediaType, status, rank, mean, numScoringUsers,
        numFavorites, popularity, startDate, endDate, synopsis) = when (item) {
        is Anime -> with(item) {
            Log.d("detailScreen","anime!")
            Tuple14(
                id, title, mainPictureLarge, genres, mediaType, status, rank,
                mean, numScoringUsers, numFavorites, popularity, startDate, endDate, synopsis
            )
        }
        is Manga -> with(item) {
            Log.d("detailScreen","manga!")
            Tuple14(
                id, title, mainPictureLarge, genres, mediaType, status, rank,
                mean, numScoringUsers, numFavorites, popularity, startDate, endDate, synopsis
            )
        }
        else -> {Log.d("detailScreen","baapre!")
            return@Scaffold
        }
    }
    Log.d("detailScreen","All goofd!")
    LazyColumn(
        modifier = padding
            .fillMaxSize()
         //   .background(Color(0xFFF4F4F4))  // Light gray background
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth() .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Cover Image
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(3f / 4f)
                            .wrapContentHeight()
                            .clip(RoundedCornerShape(12.dp))

                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Title + Watchlist
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = Color(0xFFD32F2F),
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        when(item){
                            is Anime -> {
                                WatchlistButton(animeId = id, animeTitle = title, viewModel = viewModel)
                            }
                            is Manga ->{
                                ReadListButton(id,title,viewModel = viewModel)
                            }
                        }

                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(thickness = 1.dp, color = Color(0xFFDDDDDD))

                    Spacer(modifier = Modifier.height(12.dp))

                    // Meta Info
                    Log.d("detailScreen","All goofd! 1")
                    DetailGrid(
                        items = listOf(
                            "Genres" to genres?.joinToString(", "),
                            "Media Type" to mediaType,
                            "Status" to status,
                            "Rank" to rank?.toString(),
                            "Mean Score" to mean?.toString(),
                            "Scoring Users" to numScoringUsers?.toString(),
                            "Favorites" to numFavorites?.toString(),
                            "Popularity" to popularity?.toString(),
                            "Start Date" to startDate,
                            "End Date" to endDate
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(thickness = 1.dp, color = Color(0xFFDDDDDD))

                    Spacer(modifier = Modifier.height(16.dp))

                    // Synopsis
                    Text(
                        text = "Synopsis",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = synopsis ?: "No synopsis available.",
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}
}

@Composable
fun DetailGrid(items: List<Pair<String, String?>>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Log.d("detailScreenGrid","All goofd! 1")
        items.forEach { (label, value) ->
            if (!value.isNullOrBlank()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "$label:",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}



data class Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>(
    val a: A, val b: B, val c: C, val d: D, val e: E, val f: F, val g: G,
    val h: H, val i: I, val j: J, val k: K, val l: L, val m: M, val n: N
)

@Preview(showBackground = true)
@Composable
fun MediaDetailContentPreview() {
    val fakeManga = Manga(
        id = 1234,
        title = "One Piece",
        mediaType = "Manga",
        mean = 9.12,
        numScoringUsers = 2000000,
        status = "Publishing",
        numVolumes = 105,
        numChapters = 1085,
        startDate = "1997-07-22",
        endDate = null,
        numListUsers = 3000000,
        popularity = 1,
        numFavorites = 500000,
        rank = 1,
        genres = listOf("Adventure", "Fantasy", "Shounen"),
        authors = "Eiichiro Oda",
        synopsis = "Follows the adventures of Monkey D. Luffy and his pirate crew in search of the legendary treasure, the One Piece.",
        nsfw = "White",
        createdAt = "2020-01-01",
        updatedAt = "2023-01-01",
        mainPictureMedium = "https://cdn.myanimelist.net/images/manga/3/55539.jpg",
        mainPictureLarge = "https://cdn.myanimelist.net/images/manga/3/55539l.jpg",
        alternativeTitlesEn = "One Piece",
        alternativeTitlesJa = "ワンピース",
        alternativeTitlesSynonyms = "OP"
    )

    MediaDetailContent(item = fakeManga, viewModel = null){

    }
}

