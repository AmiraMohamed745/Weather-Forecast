package com.example.weatherforecast.settings.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.weatherforecast.R
import com.example.weatherforecast.navigation.NavigationRouter
import com.example.weatherforecast.navigation.viewmodel.NavigationViewModel
import com.example.weatherforecast.ui.theme.composables.Dimensions
import com.example.weatherforecast.ui.theme.composables.TextMediumBlack

@Composable
fun SettingsScreenWithMapDisplayed(navigationViewModel: NavigationViewModel) {
    val displayMap = rememberSaveable { mutableStateOf(false) }
    if (displayMap.value) {
        MapScreen { latitude, longitude ->
            Log.d("MapScreen", "User selected location: $latitude, $longitude")
            displayMap.value = false
        }
    } else {
        SettingsScreen(
            navigationViewModel = navigationViewModel,
            onLocationOptionsSelected = { option ->
                when (option) {
                    SettingsOptions.LOCATION.radioOptions[0] -> {}
                    SettingsOptions.LOCATION.radioOptions[1] -> displayMap.value = true
                }
            })
    }
}


@Composable
fun SettingsScreen(
    navigationViewModel: NavigationViewModel,
    onLocationOptionsSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = Dimensions.paddingExtraLarge,
                bottom = Dimensions.paddingMediumExtraLarge,
                start = Dimensions.paddingModerate,
                end = Dimensions.paddingModerate
            )
    ) {
        navigationViewModel.setCurrentScreen(NavigationRouter.SettingsScreen)
        SettingsOption(
            optionTitle = SettingsOptions.LANGUAGE.displayName,
            radioOptions = SettingsOptions.LANGUAGE.radioOptions
        ) { }

        SettingsOption(
            optionTitle = SettingsOptions.LOCATION.displayName,
            radioOptions = SettingsOptions.LOCATION.radioOptions
        ) { option -> onLocationOptionsSelected(option) }

        SettingsOption(
            optionTitle = SettingsOptions.WIND_SPEED_UNIT.displayName,
            radioOptions = SettingsOptions.WIND_SPEED_UNIT.radioOptions
        ) { }

        SettingsOption(
            optionTitle = SettingsOptions.TEMPERATURE_UNIT.displayName,
            radioOptions = SettingsOptions.TEMPERATURE_UNIT.radioOptions
        ) { }
    }
}

@Composable
fun SettingsOption(
    optionTitle: String,
    radioOptions: List<String>,
    onOptionSelected: (String) -> Unit
) {
    val (selectedOption, setSelectedOption) = rememberSaveable { mutableStateOf(radioOptions[0]) }
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingModerate)
        ) {
            Icon(
                painter = painterResource(
                    when (optionTitle) {
                        SettingsOptions.LANGUAGE.displayName -> R.drawable.ic_language
                        SettingsOptions.LOCATION.displayName -> R.drawable.ic_location
                        SettingsOptions.WIND_SPEED_UNIT.displayName -> R.drawable.ic_wind_speed
                        else -> R.drawable.ic_temp_unit
                    }

                ),
                contentDescription = stringResource(R.string.switch_language_icon)
            )
            TextMediumBlack(optionTitle)
        }
        Row(modifier = Modifier.selectableGroup()) {
            radioOptions.forEach { option ->
                Row(
                    Modifier
                        .weight(1f)
                        .height(56.dp)
                        .selectable(
                            selected = (option == selectedOption),
                            onClick = {
                                setSelectedOption(option)
                                onOptionSelected(option)
                            },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingVerySmall)
                ) {
                    RadioButton(
                        selected = (option == selectedOption),
                        onClick = null
                    )
                    TextMediumBlack(option)
                }
            }

        }
    }
}