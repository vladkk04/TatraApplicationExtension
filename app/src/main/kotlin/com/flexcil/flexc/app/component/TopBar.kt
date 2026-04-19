@file:OptIn(ExperimentalMaterial3Api::class)

package com.flexcil.flexc.app.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flexcil.flexc.app.MainViewModel
import com.flexcil.flexc.app.R

@Composable
fun TopBar(
    isChanging: Boolean = false,
    isBackButton: Boolean = false
) {
    val viewModel = hiltViewModel<MainViewModel>()

    val backgroundColor = MaterialTheme.colorScheme.background
    val accentColor = MaterialTheme.colorScheme.primary

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            scrolledContainerColor = Color.Unspecified,
            navigationIconContentColor = Color.Unspecified,
            titleContentColor = Color.Unspecified,
            actionIconContentColor = Color.Unspecified
        ),
        title = {
            if (!isChanging) {
                Image(
                    painter = painterResource(id = R.drawable.tatra_logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(32.dp)
                )
            } else {
                Text("Shared Expenses")
            }
        },
        navigationIcon = {
            // FIX: If it IS a back button, show the arrow and call navigateBack
            if (isBackButton) {
                IconButton(
                    onClick = viewModel::navigateBack, // <-- Corrected action
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Go Back", // <-- Updated description
                        tint = accentColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
            } else {
                // FIX: If it is NOT a back button, show the email icon and do mail click
                IconButton(
                    onClick = { /* Handle mail click */ }, // <-- Corrected action
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Messages",
                        tint = accentColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        actions = {
            /* if(!isChanging) {
                 Text(
                     text = "Customize",
                     color = accentColor,
                     fontSize = 16.sp,
                     fontWeight = FontWeight.Normal,
                     modifier = Modifier
                         .clickable { *//* Handle Customize click *//* }
                        .padding(end = 16.dp, start = 8.dp, top = 8.dp, bottom = 8.dp)
                )*/
        }
    )
}