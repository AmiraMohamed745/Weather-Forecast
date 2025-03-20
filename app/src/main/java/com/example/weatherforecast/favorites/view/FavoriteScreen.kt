package com.example.weatherforecast.favorites.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weatherforecast.navigation.NavigationRouter
import com.example.weatherforecast.navigation.viewmodel.NavigationViewModel
import com.example.weatherforecast.ui.theme.composables.Dimensions
import com.example.weatherforecast.ui.theme.composables.FavoriteCard
import com.example.weatherforecast.ui.theme.composables.TextMediumBlack

@Composable
fun FavoriteScreen(navigationViewModel: NavigationViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                top = Dimensions.paddingExtraLarge,
                bottom = Dimensions.paddingMediumExtraLarge,
                start = Dimensions.paddingModerate,
                end = Dimensions.paddingModerate
            )
    ) {
        navigationViewModel.setCurrentScreen(NavigationRouter.FavoriteScreen)
        TextMediumBlack("You haven't added any favorites yet.")
        FavoritePlace()
    }
}

@Composable
fun FavoritePlace() {
    FavoriteCard("Egypt", "Giza, 6th October")
}

