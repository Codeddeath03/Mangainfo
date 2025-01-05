package com.example.manganese

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import com.example.manganese.ui.theme.ManganeseTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.manganese.database.entities.MangaSummary
import com.example.manganese.screens.MangaDetailScreen
import com.example.manganese.screens.MangaMainScreen


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        /*
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("https://picsum.photos/200")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()
                val responseCode = connection.responseCode
                Log.d("NETWORK_TEST", "Response Code: $responseCode")
                connection.disconnect()
            } catch (e: Exception) {
                Log.e("NETWORK_TEST", "Error: ${e.message}")
            }
        }*/

        setContent {
            val mangaDao = MangaDatabase.getDatabase(this).mangaDao()
            var mangaList by remember { mutableStateOf<List<MangaSummary>>(emptyList()) }

            LaunchedEffect(Unit) {
                mangaList = withContext(Dispatchers.IO) {
                    Log.d("MangaListScreen", Thread.currentThread().name)
                    mangaDao.getMangaSummaries()
                }
            }
            ManganeseTheme {
                App(mangaList,mangaDao)
            }
        }
    }
}

@Composable
fun App(mangaList: List<MangaSummary>, mangaDao: MangaDao) {

    val navController = rememberNavController()
    NavHost(navController= navController, startDestination = "mangaMainScreen") {
        composable("mangaMainScreen") {
            if (mangaList.isNotEmpty()) {
                MangaMainScreen(mangaList){
                    //do nothing fn
                    Log.d("MangaListScreen", "Manga ID: $it")
                    navController.navigate("detailScreen/$it")
                }
            }
            else{
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize(1F)
                ){
                    Text(text = "Loading.....")
                }
            }
        }

        //detailed screen routes
        composable(
            route = "detailScreen/{mangaId}",
            arguments = listOf(navArgument("mangaId") { type = NavType.IntType }) // Or NavType.IntType if ID is an integer
        ) { backStackEntry ->
            val mangaId = backStackEntry.arguments?.getInt("mangaId") // Or getInt if ID is an integer

            if (mangaId != null) {
                MangaDetailScreen(mangaId = mangaId,mangaDao)
            } else {
                Text(
                    text = "Manga not found",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
                )
            }
        }
    }
}
