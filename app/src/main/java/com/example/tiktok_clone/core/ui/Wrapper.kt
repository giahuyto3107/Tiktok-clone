package com.example.tiktok_clone.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBasket
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import com.example.tiktok_clone.R

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
            .background(color = if (selectedIndex == 0) Color.Black else Color.White)
            .padding(
                horizontal = dimensionResource(R.dimen.spacing_m),
                vertical = dimensionResource(R.dimen.spacing_xs)
            )
            .navigationBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        BottomNavigationItem(
            name = "Home",
            icon = if (selectedIndex == 0) Icons.Filled.Home else Icons.Outlined.Home,
            isSelected = selectedIndex == 0,
            onTap = { onTapSelected(0) }
        )

        BottomNavigationItem(
            name = "Shop",
            icon = if (selectedIndex == 1) Icons.Filled.ShoppingBasket else Icons.Outlined.ShoppingBasket,
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
            icon = if (selectedIndex == 3) Icons.Filled.Mail else Icons.Outlined.Mail,
            isSelected = selectedIndex == 3,
            onTap = { onTapSelected(3) }
        )

        BottomNavigationItem(
            name = "Profile",
            icon = if (selectedIndex == 4) Icons.Filled.Person else Icons.Outlined.Person,
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
            tint = if (isSelected && name == "Home")
                colorResource(
                    R.color.text_on_dark
                )
            else if (isSelected)
                colorResource(
                    R.color.text_on_light
                )
            else colorResource(
                R.color.text_secondary
            ),
            modifier = Modifier.size(size = dimensionResource(R.dimen.font_title_m))
        )

        Text(
            name,
            color = if (isSelected && name == "Home")
                colorResource(
                    R.color.text_on_dark
                )
            else colorResource(
                R.color.text_secondary
            ),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}