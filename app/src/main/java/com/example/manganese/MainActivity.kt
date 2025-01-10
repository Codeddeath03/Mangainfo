package com.example.manganese

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.example.manganese.ui.theme.ManganeseTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.manganese.database.entities.MangaSummary
import com.example.manganese.screens.DetailScreen
import com.example.manganese.screens.MangaMainScreen
import com.example.manganese.components.BottomNavigation
import com.example.manganese.database.entities.AnimeSummary


data class TabBarItem(
    val title: String,
    val screen: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
    val badgeAmount: Int? = null
)

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
            var animeList by remember { mutableStateOf<List<AnimeSummary>>(emptyList()) }
            LaunchedEffect(Unit) {
                mangaList = withContext(Dispatchers.IO) {
                    Log.d("MangaListScreen", Thread.currentThread().name)
                    mangaDao.getMangaSummaries()
                }
            }
            LaunchedEffect(Unit) {
                animeList = withContext(Dispatchers.IO) {
                    Log.d("AnimeListScreen", Thread.currentThread().name)
                    mangaDao.getAnimeSummaries()
                }
            }
            ManganeseTheme {
                App(mangaList,animeList,mangaDao)
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App(mangaList: List<MangaSummary>, animeList: List<AnimeSummary>, mangaDao: MangaDao) {

    // setting up the individual tabs
    val mangaTab = TabBarItem(title = "Manga", screen = "mangaMainScreen",selectedIcon = painterResource(id = R.drawable.book), unselectedIcon =painterResource(id = R.drawable.book))
    val animeTab = TabBarItem(title = "Anime", screen = "animeMainScreen",selectedIcon = painterResource(id = R.drawable.television), unselectedIcon = painterResource(id = R.drawable.television))
    // creating a list of all the tabs
    val tabBarItems = listOf(mangaTab, animeTab)

    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            val currentRoute = currentRoute(navController)
            if (currentRoute?.startsWith("detailScreen") != true){
            BottomNavigation(tabBarItems, navController) }
        }
    ){  innerPadding ->
        NavHost(navController= navController,
                startDestination = "mangaMainScreen",
                modifier = Modifier.padding(innerPadding)
        ) {
            composable("mangaMainScreen") {
                if (mangaList.isNotEmpty()) {
                    MangaMainScreen(mangaList){
                        //do nothing fn
                        Log.d("MangaListScreen", "Manga ID: $it")
                        navController.navigate("detailScreen/manga/$it")
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
            composable("animeMainScreen") {
                if (animeList.isNotEmpty()) {
                    MangaMainScreen(animeList){
                        //do nothing fn
                        Log.d("AnimeListScreen", "Anime ID: $it")
                        navController.navigate("detailScreen/anime/$it")
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



            //detailed screen route

            composable(
                route = "detailScreen/{type}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType },navArgument("type") { type = NavType.StringType }) // Or NavType.IntType if ID is an integer
            ) { backStackEntry ->
                val Id = backStackEntry.arguments?.getInt("id") // Or getInt if ID is an integer
                val contentType = backStackEntry.arguments?.getString("type")
                if (Id != null) {
                    DetailScreen(Id = Id,mangaDao,type=contentType, onBackAction = { navController.navigateUp()})
                } else {
                    Text(
                        text = "Manga/Anime not found",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
                    )
                }
            }
        }
    }

}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
