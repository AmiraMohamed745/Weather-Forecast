package com.example.weatherforecast.home.view

import android.location.Location
import androidx.compose.runtime.getValue
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherforecast.utils.Constants
import com.example.weatherforecast.ui.theme.composables.Dimensions
import com.example.weatherforecast.navigation.NavigationRouter
import com.example.weatherforecast.navigation.viewmodel.NavigationViewModel
import com.example.weatherforecast.R
import com.example.weatherforecast.home.viewmodel.CurrentWeatherViewModel
import com.example.weatherforecast.home.viewmodel.NextFiveDaysWeatherViewModel
import com.example.weatherforecast.ui.theme.composables.TextExtraLarge
import com.example.weatherforecast.ui.theme.composables.TextLarge
import com.example.weatherforecast.ui.theme.composables.TextMediumBlack
import com.example.weatherforecast.ui.theme.composables.TextSmallBlack
import com.example.weatherforecast.model.CurrentWeatherResponse
import com.example.weatherforecast.model.ForecastItem
import com.example.weatherforecast.model.NextFiveDaysWeatherResponse
import com.example.weatherforecast.ui.theme.composables.LoadingIndicator
import com.example.weatherforecast.ui.theme.composables.TextMediumWhite
import com.example.weatherforecast.ui.theme.composables.TextSmallWhite
import com.example.weatherforecast.utils.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/*
To do:
1. Provide an icon for feels like if the API doesn't
2. Make the time on hourly details non-military time
3. Change the UI of hourly details card
4. Possibly make it next 4 days since the first day is today
5. Change position of temperature mark 0c
6. Maybe provide more details on the next 5 days cards
 */

@Composable
fun HomeScreen(
    location: Location?,
    currentWeatherViewModel: CurrentWeatherViewModel,
    nextFiveDaysWeatherViewModel: NextFiveDaysWeatherViewModel,
    navigationViewModel: NavigationViewModel
) {
    val currentWeatherUiState by currentWeatherViewModel.currentWeatherState.collectAsStateWithLifecycle()
    val nextFiveDaysWeatherUiState by nextFiveDaysWeatherViewModel.nextFiveDaysWeatherState.collectAsStateWithLifecycle()
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
        navigationViewModel.setCurrentScreen(NavigationRouter.HomeScreen)

        if (location != null) {
            when (currentWeatherUiState) {
                is Response.Loading -> {
                    LoadingIndicator()
                }

                is Response.Success -> {
                    CurrentWeatherDetails(((currentWeatherUiState as Response.Success).data))
                    DailyDetails(((currentWeatherUiState as Response.Success).data))
                    Spacer(modifier = Modifier.Companion.height(Dimensions.paddingModerate))
                }

                is Response.Failure -> {
                    CurrentWeatherDetailsFailure()
                    DailyDetailsFailure()
                    Spacer(modifier = Modifier.Companion.height(Dimensions.paddingModerate))
                }
            }
            when (nextFiveDaysWeatherUiState) {
                is Response.Loading -> {
                    LoadingIndicator()
                }

                is Response.Success -> {
                    HourlyDetails(((nextFiveDaysWeatherUiState as Response.Success).data))
                    Spacer(modifier = Modifier.Companion.height(Dimensions.paddingModerate))
                    NextFiveDaysDetails(((nextFiveDaysWeatherUiState as Response.Success).data))
                }

                is Response.Failure -> {
                    HourlyDetailsFailure()
                    Spacer(modifier = Modifier.Companion.height(Dimensions.paddingModerate))
                    NextFiveDaysDetailsFailure()
                }
            }
        } else {
            HomeScreenLocationFailure()
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CurrentWeatherDetails(currentWeatherResponse: CurrentWeatherResponse?) {

    val dateFormat = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")

    val currentDateTime =
        dateFormat.format(Date((currentWeatherResponse?.dt?.toLong() ?: 0L) * 1000))
    val sunriseTime = timeFormat.format(
        Date(
            (currentWeatherResponse?.sys?.sunrise?.toLong() ?: 0L) * 1000
        )
    )
    val sunsetTime = timeFormat.format(
        Date(
            (currentWeatherResponse?.sys?.sunset?.toLong() ?: 0L) * 1000
        )
    )

    val iconCode = currentWeatherResponse?.weather?.firstOrNull()?.icon ?: "01d"
    val iconUrl = "${Constants.ICON_URL}$iconCode${Constants.ICON_URL_ENDING}"

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
            ) { // the icon is not showing!
                GlideImage(
                    model = iconUrl,
                    contentDescription = stringResource(R.string.current_weather_icon),
                    modifier = Modifier.Companion
                        .size(Dimensions.iconSize)
                )
                TextMediumBlack(currentWeatherResponse?.weather?.firstOrNull()?.description.toString())
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
                    currentWeatherResponse?.main?.feelsLike.toString()
                )
            )
            TextSmallBlack(currentDateTime)
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
            TextExtraLarge(currentWeatherResponse?.main?.temp.toString())
            TextMediumBlack("${currentWeatherResponse?.name}, ${currentWeatherResponse?.sys?.country.toString()}")
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
                    TextSmallBlack(stringResource(R.string.sunrise, sunriseTime))
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
                    TextSmallBlack(stringResource(R.string.sunset, sunsetTime))
                }

            }
        }
        Spacer(modifier = Modifier.Companion.height(Dimensions.paddingMediumLarge))

    }
}

@Composable
fun HourlyDetails(nextFiveDaysWeatherResponse: NextFiveDaysWeatherResponse) {
    Column {
        TextLarge(stringResource(R.string.hourly_details))
        Spacer(modifier = Modifier.Companion.height(Dimensions.paddingModerate))
        LazyRow(
            contentPadding = PaddingValues(horizontal = Dimensions.paddingSmall),
            horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall)
        ) {
            items(Constants.HOURLY_DETAILS_CARDS) { index ->
                val forecastItemToDisplay = nextFiveDaysWeatherResponse.forecastItem.get(index)
                val timeFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())
                val formattedTime =
                    timeFormat.format(Date(forecastItemToDisplay.dt?.toLong()?.times(1000) ?: 0L))
                HourlyDetailsCard(
                    formattedTime.toString(),
                    forecastItemToDisplay.main?.temp.toString()
                )
            }
        }
    }
}

@Composable
fun HourlyDetailsCard(time: String, temperature: String) {
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
            TextSmallWhite(text = time)
            TextSmallWhite(text = temperature)
        }
    }
}

@Composable
fun DailyDetails(currentWeatherResponse: CurrentWeatherResponse?) {
    Column {
        TextLarge(stringResource(R.string.daily_details))
        Spacer(modifier = Modifier.Companion.height(Dimensions.paddingModerate))
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall),
            modifier = Modifier.padding(bottom = Dimensions.paddingModerate)
        )
        {
            DailyDetailsCard(currentWeatherResponse)
        }
    }
}

@Composable
fun DailyDetailsCard(currentWeatherResponse: CurrentWeatherResponse?) {
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
                TextSmallWhite(currentWeatherResponse?.main?.pressure.toString())
                TextSmallWhite(currentWeatherResponse?.wind?.speed.toString())
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
                /*Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        Dimensions.paddingVerySmall
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_rain),
                        contentDescription = stringResource(R.string.rain_icon)
                    )
                    TextMediumWhite("Rain")
                }*/

            }
            Spacer(modifier = Modifier.height(Dimensions.paddingSmall))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextSmallWhite(currentWeatherResponse?.main?.humidity.toString())
                //TextSmallWhite(currentWeatherResponse?.rain.toString())
                TextSmallWhite(currentWeatherResponse?.clouds?.all.toString())
            }
        }

    }
}

@Composable
fun NextFiveDaysDetails(nextFiveDaysWeatherResponse: NextFiveDaysWeatherResponse?) {
    Column {
        TextLarge(stringResource(R.string.next_5_days))
        Spacer(modifier = Modifier.Companion.height(Dimensions.paddingModerate))
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall),
            modifier = Modifier.padding(bottom = Dimensions.paddingModerate)
        )
        {
            repeat(Constants.NEXT_DAYS) { dayIndex ->
                val index = dayIndex * 8
                val forecastItem = nextFiveDaysWeatherResponse?.forecastItem?.get(index)
                NextFiveDaysWeatherCard(forecastItem)
            }
        }
    }
}

@Composable
fun NextFiveDaysWeatherCard(forecastItem: ForecastItem?) {

    val timestamp = forecastItem?.dt
    val dayToDateTime = formatDate(timestamp)
    val dayName = dayToDateTime.first
    val dateTime = dayToDateTime.second

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
                TextMediumWhite(dayName)
                TextMediumWhite(stringResource(R.string.feels_like_with_temp_beneath_it))
            }
            Spacer(modifier = Modifier.height(Dimensions.paddingSmall))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextSmallWhite(dateTime)
                TextSmallWhite(forecastItem?.main?.feelsLike.toString())
            }
        }

    }
}

private fun formatDate(timestamp: Int?): Pair<String, String> {
    val timeStampLong = timestamp?.toLong()?.times(1000) ?: 0L
    val day = SimpleDateFormat("EEEE", Locale.getDefault())
    day.timeZone = TimeZone.getDefault()
    val formattedDay = day.format(timeStampLong)
    val dateTime =
        SimpleDateFormat("MMM d, yyyy", Locale.getDefault())/*.format(timestamp?.times(1000))*/
    dateTime.timeZone = TimeZone.getDefault()
    val formattedDateTime = dateTime.format(timeStampLong)
    return formattedDay to formattedDateTime
}



