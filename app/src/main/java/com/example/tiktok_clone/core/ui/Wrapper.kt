package com.example.tiktok_clone.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import com.example.tiktok_clone.R
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Envelope
import compose.icons.fontawesomeicons.solid.Home
import compose.icons.fontawesomeicons.solid.ShoppingBag
import compose.icons.fontawesomeicons.solid.User

@Composable
fun MainWrapper(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    onCameraClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            content()
        }

        BottomNavigationBar(
            selectedIndex = selectedIndex,
            onTapSelected = onTabSelected,
            onCameraClick = onCameraClick
        )
    }
}

@Composable
private fun BottomNavigationBar(
    selectedIndex: Int,
    onTapSelected: (Int) -> Unit,
    onCameraClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(R.dimen.spacing_m)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        BottomNavigationItem(
            name = "Home",
            icon = FontAwesomeIcons.Solid.Home,
            isSelected = selectedIndex == 0,
            onTap = { onTapSelected(0) }
        )

        BottomNavigationItem(
            name = "Shop",
            icon = FontAwesomeIcons.Solid.ShoppingBag,
            isSelected = selectedIndex == 1,
            onTap = { onTapSelected(1) }
        )

        Image(
            painter = painterResource(id = R.drawable.camera_button_3x),
            contentDescription = "Camera button",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(bottom = dimensionResource(R.dimen.spacing_s))
                .clickable { onCameraClick() }
        )

        BottomNavigationItem(
            name = "Inbox",
            icon = FontAwesomeIcons.Solid.Envelope,
            isSelected = selectedIndex == 3,
            onTap = { onTapSelected(3) }
        )

        BottomNavigationItem(
            name = "Profile",
            icon = FontAwesomeIcons.Solid.User,
            isSelected = selectedIndex == 4,
            onTap = { onTapSelected(4) }
        )
    }
}

@Composable
private fun BottomNavigationItem(
    name: String,
    icon: ImageVector,
    onTap: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.clickable { onTap() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = name,
            tint = if (isSelected) colorResource(R.color.text_on_dark) else colorResource(R.color.text_secondary),
            modifier = Modifier.size(size = dimensionResource(R.dimen.font_title_m))
        )

        Text(
            name,
            color = if (isSelected) colorResource(R.color.text_on_dark) else colorResource(R.color.text_secondary),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}