package com.example.manganese.components

import android.util.Log
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.manganese.TabBarItem
import androidx.compose.ui.graphics.painter.Painter
import com.example.manganese.navigateToSingleTop
import com.example.manganese.screens.profilePopUp


@Composable
fun BottomNavigation(
    tabBarItems: List<TabBarItem>,
    selectedTabIndex: Int, onTabSelected: (Int) -> Unit,navController: NavHostController, viewModel: UserViewModel
) {

    val nickname by viewModel.nickname.collectAsState()
   // Log.d("nickname","$nickname")
  //  Log.d("bottomnav","$selectedTabIndex")
    NavigationBar {
            // looping over each tab to generate the views and navigation for each item
            tabBarItems.forEachIndexed { index, tabBarItem ->

                NavigationBarItem(
                    selected = selectedTabIndex == index,
                    onClick = {
                        onTabSelected(index)
                        navController.navigateToSingleTop(tabBarItem.screen)
                    },
                    icon = {
                        TabBarIconView(
                            isSelected = selectedTabIndex == index,
                            selectedIcon = tabBarItem.selectedIcon,
                            unselectedIcon = tabBarItem.unselectedIcon,
                            title = tabBarItem.title,
                            badgeAmount = tabBarItem.badgeAmount
                        )
                    },
                    label = {Text(tabBarItem.title)}
                )
            }
        profilePopUp("${nickname.data}", onWatchListclick = {
           // Log.d("anime click","GE fighting")
            navController.navigateToSingleTop("watchlistScreen")
        },
            onReadListClick = {
                // Log.d("manga click","GE fighting")
                navController.navigateToSingleTop("readlistScreen")
            }){
            navController.navigateToSingleTop("settingsScreen")
        }
        }
}

@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: Painter,
    unselectedIcon: Painter,
    title: String,
    badgeAmount: Int? = null,

) {
    BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
        Icon(
            painter = if (isSelected) {selectedIcon} else {unselectedIcon},
            contentDescription = title
        )
    }
}


@Composable
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(count.toString())
        }
    }
}
