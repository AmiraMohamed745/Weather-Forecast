package com.example.weatherforecast.home.view

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherforecast.R
import com.example.weatherforecast.model.CurrentWeatherResponse
import com.example.weatherforecast.model.NextFiveDaysWeatherResponse
import com.example.weatherforecast.ui.theme.composables.Dimensions
import com.example.weatherforecast.ui.theme.composables.TextExtraLarge
import com.example.weatherforecast.ui.theme.composables.TextLarge
import com.example.weatherforecast.ui.theme.composables.TextMediumBlack
import com.example.weatherforecast.ui.theme.composables.TextMediumWhite
import com.example.weatherforecast.ui.theme.composables.TextSmallBlack
import com.example.weatherforecast.ui.theme.composables.TextSmallWhite
import com.example.weatherforecast.utils.Constants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/*
To do:
1. Provide UI for when there is no internet, thus complete API failure
2. UI for when no location is provided
 */

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CurrentWeatherDetailsFailure() {
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    Dimensions.paddingVerySmall
                )
            ) {
                GlideImage(
                    model = "",
                    contentDescription = "",
                    modifier = Modifier.Companion
                        .size(Dimensions.iconSize)
                )
                TextMediumBlack(stringResource(R.string.n_a))
            }

            TextMediumBlack(stringResource(R.string.today))
        }
        Spacer(modifier = Modifier.Companion.height(Dimensions.paddingVerySmall))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextSmallBlack(
                stringResource(
                    R.string.feels_like,
                    R.string.n_a
                )
            )
            TextSmallBlack(stringResource(R.string.n_a))
        }
        Spacer(modifier = Modifier.Companion.height(Dimensions.paddingLarge))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(start = Dimensions.startPaddingForTemperatureMark)) {
                TextMediumBlack("°C")
            }
            TextExtraLarge(stringResource(R.string.n_a))
            TextMediumBlack(stringResource(R.string.n_a))
            Spacer(modifier = Modifier.Companion.width(Dimensions.paddingVerySmall))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingVerySmall)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_sunrise),
                        contentDescription = stringResource(R.string.sunrise_icon)
                    )
                    TextSmallBlack(stringResource(R.string.sunrise, stringResource(R.string.n_a)))
                }

                Spacer(modifier = Modifier.Companion.width(Dimensions.paddingModerate))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        Dimensions.paddingVerySmall
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_sunset),
                        contentDescription = stringResource(R.string.sunset_icon)
                    )
                    TextSmallBlack(stringResource(R.string.sunset, stringResource(R.string.n_a)))
                }

            }
        }
        Spacer(modifier = Modifier.Companion.height(Dimensions.paddingMediumLarge))
    }
}

@Composable
fun HourlyDetailsFailure() {
    Column {
        TextLarge(stringResource(R.string.hourly_details))
        Spacer(modifier = Modifier.Companion.height(Dimensions.paddingModerate))
        LazyRow(
            contentPadding = PaddingValues(horizontal = Dimensions.paddingSmall),
            horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall)
        ) {
            items(Constants.HOURLY_DETAILS_CARDS) {
                HourlyDetailsCardFailure()
            }
        }
    }
}


@Composable
fun HourlyDetailsCardFailure() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier
            .size(width = Dimensions.cylinderCardWidth, height = Dimensions.cylinderCardHeight)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = Dimensions.paddingSmall)
        ) {
            TextSmallWhite(stringResource(R.string.n_a))
            TextSmallWhite(stringResource(R.string.n_a))
        }
    }
}

@Composable
fun DailyDetailsFailure() {
    Column {
        TextLarge(stringResource(R.string.daily_details))
        Spacer(modifier = Modifier.Companion.height(Dimensions.paddingModerate))
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall),
            modifier = Modifier.padding(bottom = Dimensions.paddingModerate)
        )
        {
            DailyDetailsCardFailure()
        }
    }
}

@Composable
fun DailyDetailsCardFailure() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.rectangleCardMultipleInformationHeight)
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.paddingMedium)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        Dimensions.paddingVerySmall
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_air_pressure),
                        contentDescription = stringResource(R.string.air_pressure_icon)
                    )
                    TextMediumWhite(stringResource(R.string.pressure))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        Dimensions.paddingVerySmall
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_wind_speed),
                        contentDescription = stringResource(R.string.wind_speed_icon)
                    )
                    TextMediumWhite(stringResource(R.string.wind_speed))
                }


            }
            Spacer(modifier = Modifier.height(Dimensions.paddingSmall))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextSmallWhite(stringResource(R.string.n_a))
                TextSmallWhite(stringResource(R.string.n_a))
            }
            Spacer(modifier = Modifier.height(Dimensions.paddingModerate))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        Dimensions.paddingVerySmall
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_humidity),
                        contentDescription = stringResource(R.string.humidity_icon)
                    )
                    TextMediumWhite(stringResource(R.string.humidity))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        Dimensions.paddingVerySmall
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_clouds),
                        contentDescription = stringResource(R.string.clouds_icon)
                    )
                    TextMediumWhite(stringResource(R.string.clouds))
                }
            }
            Spacer(modifier = Modifier.height(Dimensions.paddingSmall))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextSmallWhite(stringResource(R.string.n_a))
                TextSmallWhite(stringResource(R.string.n_a))
            }
        }

    }
}

@Composable
fun NextFiveDaysDetailsFailure() {
    Column {
        TextLarge(stringResource(R.string.next_5_days))
        Spacer(modifier = Modifier.Companion.height(Dimensions.paddingModerate))
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall),
            modifier = Modifier.padding(bottom = Dimensions.paddingModerate)
        )
        {
            repeat(5) {
                NextFiveDaysWeatherCardFailure()
            }
        }
    }
}

@Composable
fun NextFiveDaysWeatherCardFailure() {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.rectangleCardHeight)
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.paddingMedium)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextMediumWhite(stringResource(R.string.n_a))
                TextMediumWhite(stringResource(R.string.feels_like_with_temp_beneath_it))
            }
            Spacer(modifier = Modifier.height(Dimensions.paddingSmall))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextSmallWhite(stringResource(R.string.n_a))
                TextSmallWhite(stringResource(R.string.n_a))
            }
        }

    }
}

// Will change this methods name to when no internet is available
@Composable
fun HomeScreenApiFailure() {
    // Need the text to appear in the center of the screen
    Column(verticalArrangement = Arrangement.Center) {
        Text(
            text = "Sorry, we can't show weather data right now.\n" +
                    "Try again later.",
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()

        )
    }
}

@Composable
fun HomeScreenLocationFailure() {
    // Need the text to appear in the center of the screen
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No location is provided.",
            fontSize = 22.sp,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
        )
    }
}