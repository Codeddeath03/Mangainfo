package com.example.manganese.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.manganese.R


@Composable
fun profilePopUp(nickname: String,onWatchListclick:()-> Unit,onReadListClick: () -> Unit,onSettingsClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(painterResource(id = R.drawable.profile), contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // First section
            DropdownMenuItem(
                text = { Text(nickname) },
                leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                onClick = { /* Do something... */ }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Watchlist") },
                leadingIcon = { Icon(painterResource(R.drawable.watchlist), contentDescription = null) },
                onClick = {
                    expanded = false
                    onWatchListclick()}
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Readlist") },
                leadingIcon = { Icon(painterResource(R.drawable.readlist) , contentDescription = null) },
                onClick = {
                    expanded = false
                    onReadListClick()
                }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Settings") },
                leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                onClick = {
                    expanded=false
                    onSettingsClick()}
            )

/*
            HorizontalDivider()

            // Second section
            DropdownMenuItem(
                text = { Text("Send Feedback") },
                leadingIcon = { Icon(Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = null) },
                trailingIcon = { Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = null) },
                onClick = { /* Do something... */ }
            )

            HorizontalDivider()

            // Third section
            DropdownMenuItem(
                text = { Text("About") },
                leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                onClick = { /* Do something... */ }
            )
            DropdownMenuItem(
                text = { Text("Help") },
                leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                trailingIcon = { Icon(Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = null) },
                onClick = { /* Do something... */ }
            )

 */
        }
    }
}
@Composable
fun ProfileDropdownMenu(onDismiss: () -> Unit) {
    DropdownMenu(
        expanded = true,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = { Text("Profile") },
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
            onClick = { onDismiss() }  // Handle profile click (maybe navigate or show dialog)
        )
        DropdownMenuItem(
            text = { Text("Settings") },
            leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
            onClick = { onDismiss() }  // Handle settings click
        )

        HorizontalDivider()

        DropdownMenuItem(
            text = { Text("Send Feedback") },
            leadingIcon = { Icon(Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = null) },
            trailingIcon = { Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = null) },
            onClick = { onDismiss() }
        )

        HorizontalDivider()

        DropdownMenuItem(
            text = { Text("About") },
            leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null) },
            onClick = { onDismiss() }
        )
        DropdownMenuItem(
            text = { Text("Help") },
            leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null) },
            trailingIcon = { Icon(Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = null) },
            onClick = { onDismiss() }
        )
    }
}


@Preview
@Composable
private fun prevProfileScreen() {
    profilePopUp("peeko-park", onWatchListclick = {}, onReadListClick = {}, onSettingsClick = {})

}