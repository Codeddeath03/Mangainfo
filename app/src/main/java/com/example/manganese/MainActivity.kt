package com.example.manganese

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.example.manganese.ui.theme.ManganeseTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.manganese.database.entities.MangaSummary
import com.example.manganese.screens.DetailScreen
import com.example.manganese.screens.MangaMainScreen
import com.example.manganese.components.BottomNavigation
import com.example.manganese.components.SearchBar
import com.example.manganese.components.UserViewModel
import com.example.manganese.components.UserViewModelFactory
import com.example.manganese.database.DbViewModelFactory
import com.example.manganese.database.Resources
import com.example.manganese.database.dbRepository
import com.example.manganese.database.dbViewModel
import com.example.manganese.database.entities.AnimeSummary
import com.example.manganese.database.firedb
import com.example.manganese.screens.LoginScreen
import com.example.manganese.screens.SettingsScreen
import com.example.manganese.screens.SignUpScreen
import com.example.manganese.screens.readListScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.example.manganese.screens.watchListScreen


data class TabBarItem(
    val title: String,
    val screen: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
    val badgeAmount: Int? = null
)

//intial blueprint of the completed app

// implement login/register screen
// only logged in users are allowed to view the screens
// implement search bar
// implement filter
// implement theme switch
// implement favourites/liked anime&manga (a button for this on detailed screen as well as on main screen) //when the detailed screen view is gone we then add anime/manga to database call and on mainscreen we maintain list.
//           with this need to implement a new-user page where they can select their top 6 genres,anime's,manga's
//           pass this to recommendation model and then get the recommended anime/manga
// implement profile drawer (basically third icon/option in bottom nav)
// then start with backend for connecting recommendation model to this app
// learn retrofit and necessary things for iteracting with backend
// implement trending with mal api

//ctrl + shift + alt shortcut for multiple cursor multi lines


class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        auth = Firebase.auth

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
            val dbRepo = dbRepository(mangaDao)  // Replace this with your actual instance
            val dbViewModelFactory = DbViewModelFactory(dbRepo)

            val dbViewModel = ViewModelProvider(this, dbViewModelFactory).get(dbViewModel::class.java)
            val fireDB = firedb()
            val userViewModelFactory = UserViewModelFactory(fireDB,mangaDao)

            val userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)



           // var mangaList by remember { mutableStateOf<List<MangaSummary>>(emptyList()) }
          //  var animeList by remember { mutableStateOf<List<AnimeSummary>>(emptyList()) }

            val mangaList = dbViewModel.Mangapager.collectAsLazyPagingItems()
            val animeList = dbViewModel.Animepager.collectAsLazyPagingItems()

            val navController = rememberNavController()
            ManganeseTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    App(mangaList,animeList,mangaDao,navController,auth,userViewModel)
                }
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App(
    mangaList: LazyPagingItems<MangaSummary>,
    animeList: LazyPagingItems<AnimeSummary>,
    mangaDao: MangaDao,
    navController: NavHostController,
    auth: FirebaseAuth,
    viewModel: UserViewModel
) {
    // fix signup logic where the user registration is handled by the viewmodel also it register the new user to firestore
    //fix mangarow for the items part


    // search function separately, which will change the search state. if query typed state == true else false
    //creation of variable mlist and alist for mangalist and animelist, which will change based on the state above. if true > fetchs the filtered results and stores in mlist/alist else normal mangalist / animelist
    //in this function we collect that state from viewmodel, if true we change mlist/alist accordingly
    // so do not have to change anything for mangamainscreen and animemainscreen

    var isLoading by remember { mutableStateOf(false) }


    Log.d("MainActivity", "App launched")
    val context = LocalContext.current
    val isLoggedIn = viewModel.hasUser
    Log.d("App intialized checcking user:","$isLoggedIn")
    val nickname by viewModel.nickname.collectAsState()
    val signupState by viewModel.signupState.collectAsState()
    // setting up the individual tabs
    val mangaTab = TabBarItem(title = "Manga", screen = "mangaMainScreen",selectedIcon = painterResource(id = R.drawable.book), unselectedIcon =painterResource(id = R.drawable.book), badgeAmount = 1)
    val animeTab = TabBarItem(title = "Anime", screen = "animeMainScreen",selectedIcon = painterResource(id = R.drawable.television), unselectedIcon = painterResource(id = R.drawable.television))
    //val profileTab = TabBarItem(title = "${nickname.dat}", screen = "profileScreen",selectedIcon = painterResource(id = R.drawable.profile), unselectedIcon = painterResource(id = R.drawable.profile))

    // creating a list of all the tabs
    val tabBarItems = listOf(mangaTab, animeTab)

    Scaffold(
        topBar = {
            val currentRoute = currentRoute(navController)
            if (currentRoute?.startsWith("detailScreen") != true && currentRoute?.startsWith("LoginScreen") != true &&
                currentRoute?.startsWith("SignUpScreen") != true
            )  {
            SearchBar(
                query = "",
                onQueryChange = {},
                onClearQuery = {}
            )}
        },
        bottomBar = {
            val currentRoute = currentRoute(navController)
            if (currentRoute?.startsWith("detailScreen") != true && currentRoute?.startsWith("LoginScreen") != true &&
                currentRoute?.startsWith("SignUpScreen") != true
                )  {
            BottomNavigation(tabBarItems, navController,viewModel) }
        }
    ){  innerPadding ->
        NavHost(navController= navController,
                startDestination = if (isLoggedIn) "main_app" else "login_flow",
                modifier = Modifier.padding(innerPadding)
        ) {
            navigation(startDestination="LoginScreen",route="login_flow"){
                composable("LoginScreen") {
                    LoginScreen(onClickLogin = { username,password ->
                        auth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful){
                                    navController.navigate("main_app") {
                                        popUpTo("LoginScreen") { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(context,"Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                    Log.e("LoginScreen", "Login failed: ${task.exception?.message}")
                                }
                            }
                    },
                        onSignUpClick = {navController.navigateToSingleTop("SignUpScreen")}
                    )

                }
                composable("SignUpScreen"){
                    SignUpScreen(
                        onSignInClick = {navController.navigateToSingleTop("LoginScreen")},
                        onSignUpClick = {email,password,username ->
                            isLoading = true
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        viewModel.newUserRegister(email,password, username){
                                            //successful registration
                                            isLoading= false
                                            navController.navigate("main_app") {
                                                popUpTo("SignUpScreen") { inclusive = true }
                                            }
                                        }

                                    } else {
                                        //registration failure
                                        isLoading = false
                                        Toast.makeText(context, "Signup failed", Toast.LENGTH_SHORT).show()
                                    }
                                }

                        }
                    )

                }
            }
            navigation(startDestination = "mangaMainScreen", route = "main_app"){
                composable("mangaMainScreen") {
                    Log.d("MangaListScreen", "${mangaList.itemCount}")
                    if (mangaList.itemCount > 0) {
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
                    if (animeList.itemCount > 0) {
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
            }

//            navigation(startDestination = "profileScreen", route = "profile"){
//                composable("profileScreen") {
//                    nickname.data?.let { it1 ->
//                        profilePopUp(it1)
//                    }
//                }
//            }
            composable(route="watchlistScreen") {
                watchListScreen(viewModel){
                    navController.navigate("detailScreen/anime/$it")
                }

            }
            composable(route="readlistScreen") {
                readListScreen(viewModel){
                    navController.navigate("detailScreen/manga/$it")
                }
            }
            composable(route="settingsScreen") {
                //settings screen
                SettingsScreen(
                    onLogoutSuccess = {
                        navController.navigate("login_flow") {
                            popUpTo("settingsScreen") { inclusive = true }
                        }
                    },
                    onLogoutError = {
                        Toast.makeText(context, "Logout failed: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            //detailed screen route

            composable(
                route = "detailScreen/{type}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType },navArgument("type") { type = NavType.StringType })
            ) { backStackEntry ->
                val Id = backStackEntry.arguments?.getInt("id") // Or getInt if ID is an integer
                val contentType = backStackEntry.arguments?.getString("type")
                if (Id != null) {
                    DetailScreen(Id = Id,mangaDao,type=contentType, onBackAction = { navController.popBackStack()},viewModel)
                } else {
                    Text(
                        text = "Manga/Anime not found",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }
        }
    }
    if (isLoading) {
        Toast.makeText(context, "Creating account...", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

fun NavController.navigateToSingleTop(route:String){
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState=true
    }
}

