package com.example.weatherforecast.ui.theme.composables

import android.annotation.SuppressLint
import android.location.Location
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherforecast.alerts.view.AlertsScreen
import com.example.weatherforecast.favorites.view.FavoriteScreen
import com.example.weatherforecast.R
import com.example.weatherforecast.settings.view.SettingsScreen
import com.example.weatherforecast.home.view.HomeScreen
import com.example.weatherforecast.model.CurrentWeatherResponse
import com.example.weatherforecast.navigation.NavigationRouter
import com.example.weatherforecast.navigation.viewmodel.NavigationViewModel
import kotlinx.coroutines.launch


@Composable
fun TextSmallBlack(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun TextSmallWhite(text: String) {
    Text(
        text = text,
        color = Color.White,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun TextMediumBlack(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun TextMediumWhite(text: String) {
    Text(
        text = text,
        color = Color.White,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun TextLarge(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun TextExtraLarge(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineLarge
    )
}


@Composable
fun CardCylinder(time: String, temperature: String) {
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
fun CardRectangle() {
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
                TextMediumWhite("Today")
                TextMediumWhite("Feels like")
            }
            Spacer(modifier = Modifier.height(Dimensions.paddingSmall))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextSmallWhite("Fri, 18 Feb")
                TextSmallWhite("-7 / -15")
            }
        }

    }
}

@Composable
fun CardRectangleMultipleInformation() {
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
                TextSmallWhite("981hpa")
                TextSmallWhite("1.49 meter/second")
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
                        painter = painterResource(R.drawable.ic_wind_speed),
                        contentDescription = stringResource(R.string.wind_speed_icon)
                    )
                    TextMediumWhite("Ultraviolet")
                }*/

            }
            Spacer(modifier = Modifier.height(Dimensions.paddingSmall))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextSmallWhite("98%")
                //TextSmallWhite("0")
                TextSmallWhite("100%")
            }
        }

    }
}

@Composable
fun FavoriteCard(countryName: String, address: String) {
    Card(
        shape = RoundedCornerShape(Dimensions.paddingMediumExtraLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.favoriteCardHeight)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            //horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = Dimensions.paddingMedium)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingMedium)
            ) {
                TextMediumWhite(countryName)
                TextSmallWhite(address)
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.ic_arrow_forward),
                contentDescription = stringResource(R.string.arrow_forward_icon)
            )
        }
    }
}


/*@Composable
fun NavigationDrawer() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Weather Forecast",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                    HorizontalDivider()

                    //Text("Section 1", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                    NavigationDrawerItem(
                        label = { Text("Home") },
                        selected = false,
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        onClick = { /* Handle click */ }
                    )
                    NavigationDrawerItem(
                        label = { Text("Favorites") },
                        selected = false,
                        icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                        onClick = { /* Handle click */ }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    //Text("Section 2", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                    NavigationDrawerItem(
                        label = { Text("Alerts") },
                        selected = false,
                        icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                        // badge = { Text("20") }, // Placeholder
                        onClick = { /* Handle click */ }
                    )
                    NavigationDrawerItem(
                        label = { Text("Settings") },
                        selected = false,
                        icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                        onClick = { /* Handle click */ },
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        },
        drawerState = drawerState
    ) {}
}*/

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    locationState: MutableState<Location?>,
    weatherState: MutableState<CurrentWeatherResponse?>,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val navController = rememberNavController()
    val navigationViewModel = NavigationViewModel()
    val currentScreen = navigationViewModel.currentScreen.observeAsState()

    Box() {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Weather Forecast",
                            fontSize = 22.sp,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleLarge
                        )
                        HorizontalDivider()
                        NavigationDrawerItem(
                            label = { Text("Home") },
                            selected = false,
                            icon = { Icon(Icons.Default.Home, contentDescription = null) },
                            onClick = {
                                navController.navigate(NavigationRouter.HomeScreen)
                                scope.launch { drawerState.close() }
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text("Favorites") },
                            selected = false,
                            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                            onClick = {
                                navController.navigate(NavigationRouter.FavoriteScreen)
                                scope.launch { drawerState.close() }
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text("Alerts") },
                            selected = false,
                            icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                            onClick = {
                                navController.navigate(NavigationRouter.AlertsScreen)
                                scope.launch { drawerState.close() }
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text("Settings") },
                            selected = false,
                            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                            onClick = {
                                navController.navigate(NavigationRouter.SettingsScreen)
                                scope.launch { drawerState.close() }
                            },
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
            },
        ) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White,
                            titleContentColor = Color.Black,
                        ),
                        title = {
                            Text(
                                when (currentScreen.value) {
                                    NavigationRouter.HomeScreen -> "Home"
                                    NavigationRouter.FavoriteScreen -> "Favorites"
                                    NavigationRouter.AlertsScreen -> "Alerts"
                                    else -> "Settings"
                                },
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    if (drawerState.isOpen) {
                                        drawerState.close()
                                    } else {
                                        drawerState.open()
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = stringResource(R.string.menu_icon)
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                },
                floatingActionButton = {
                    if ((currentScreen.value == NavigationRouter.FavoriteScreen) || (currentScreen.value == NavigationRouter.AlertsScreen)) {
                        FloatingActionButtonUI(navigationViewModel)
                    }
                }
            ) {
                NavigationHost(
                    navController = navController,
                    locationState,
                    weatherState,
                    navigationViewModel = navigationViewModel
                )
            }
        }
        BackHandler(enabled = drawerState.isOpen) {
            scope.launch { drawerState.close() }
        }
    }
}


@Composable
fun NavigationHost(
    navController: NavController,
    locationState: MutableState<Location?>,
    weatherState: MutableState<CurrentWeatherResponse?>,
    navigationViewModel: NavigationViewModel
) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = NavigationRouter.HomeScreen
    ) {
        composable<NavigationRouter.HomeScreen> {
            HomeScreen(
                locationState.value,
                weatherState.value,
                navigationViewModel = navigationViewModel
            )
        }
        composable<NavigationRouter.FavoriteScreen> { FavoriteScreen(navigationViewModel = navigationViewModel) }
        composable<NavigationRouter.AlertsScreen> { AlertsScreen(navigationViewModel = navigationViewModel) }
        composable<NavigationRouter.SettingsScreen> { SettingsScreen(navigationViewModel = navigationViewModel) { } }
    }
}

@Composable
fun FloatingActionButtonUI(navigationViewModel: NavigationViewModel) {
    val currentScreen = navigationViewModel.currentScreen.observeAsState()
    FloatingActionButton(
        onClick = {
            {
                when (currentScreen.value) {
                    NavigationRouter.FavoriteScreen -> {} // show map to choose favorite place
                    else -> {} // add an alert
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.secondary
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "Floating action button."
        )
    }
}
/*@Composable
fun DrawerBackHandler(drawerState: DrawerState, scope: CoroutineScope) {
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }
}*/

/*@Composable
fun BackPressHandler(onBackPressed: () -> Unit) {
    val currentOnBackPressed by rememberUpdatedState(onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    val backDispatcher = LocalBackPressedDispatcher.current

    DisposableEffect(backDispatcher) {
        backDispatcher.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}

val LocalBackPressedDispatcher =
    staticCompositionLocalOf<OnBackPressedDispatcher> { error("No Back Dispatcher provided") }*/

@Composable
fun ButtonPrimary() {
    Button(onClick = {}) {
        TextSmallBlack("")
    }
}


