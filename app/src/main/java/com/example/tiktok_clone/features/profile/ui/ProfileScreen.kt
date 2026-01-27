package com.example.tiktok_clone.features.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen (
    modifier: Modifier = Modifier,
    onNavigationToProfileScreen: () -> Unit)
{
    Box(modifier = modifier.fillMaxSize()) {
        Column() {
            ProfileHeader()
            ProfileBody(onNavigationToProfileScreen= onNavigationToProfileScreen)
        }
    }

}

@Composable
fun ProfileHeader(modifier: Modifier = Modifier) {
    Surface {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Profile",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.headlineSmall
                )
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }
}

@Composable
fun ProfileBody(modifier: Modifier = Modifier,
                onNavigationToProfileScreen: () -> Unit) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User",)
            Text("Log into existing account")
            Button(onClick = onNavigationToProfileScreen) {
                Text("Login")
            }
        }
    }

}




@Preview
@Composable
private fun PreviewLoginScreen() {
    ProfileScreen(onNavigationToProfileScreen = {})
}