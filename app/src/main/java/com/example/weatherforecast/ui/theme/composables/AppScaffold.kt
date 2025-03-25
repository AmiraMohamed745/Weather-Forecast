package com.example.weatherforecast.ui.theme.composables

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import com.example.weatherforecast.R
import com.example.weatherforecast.alerts.view.AlertsScreen
import com.example.weatherforecast.favorites.view.FavoriteScreen
import com.example.weatherforecast.alerts.view.SettingAlertScreen
import com.example.weatherforecast.settings.view.SettingsScreen
import com.example.weatherforecast.home.view.HomeScreen
import com.example.weatherforecast.model.CurrentWeatherResponse
import com.example.weatherforecast.navigation.NavigationRouter
import com.example.weatherforecast.navigation.viewmodel.NavigationViewModel
import com.example.weatherforecast.utils.location.LocationViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherforecast.home.viewmodel.CurrentWeatherViewModel
import com.example.weatherforecast.home.viewmodel.NextFiveDaysWeatherViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    currentWeatherViewModel: CurrentWeatherViewModel,
    locationViewModel: LocationViewModel,
    nextFiveDaysWeatherViewModel: NextFiveDaysWeatherViewModel,
    navigationViewModel: NavigationViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val navController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }
    //val navigationViewModel: NavigationViewModel = viewModel()

    val currentScreen by navigationViewModel.currentScreen.collectAsStateWithLifecycle()

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
            }
        ) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                snackbarHost = { SnackbarHost(snackBarHostState) },
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White,
                            titleContentColor = Color.Black,
                        ),
                        title = {
                            Text(
                                when (currentScreen) {
                                    NavigationRouter.HomeScreen -> "Home"
                                    NavigationRouter.FavoriteScreen -> "Favorites"
                                    NavigationRouter.AlertsScreen -> "Alerts"
                                    NavigationRouter.SettingAlertScreen -> "Set Alert"
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
                    if ((currentScreen == NavigationRouter.FavoriteScreen) || (currentScreen == NavigationRouter.AlertsScreen)) {
                        FloatingActionButtonUI {
                            if (currentScreen == NavigationRouter.FavoriteScreen) {
                                //
                            } else if (currentScreen == NavigationRouter.AlertsScreen) {
                                navController.navigate(NavigationRouter.SettingAlertScreen)
                            }
                        }
                    }
                }
            ) {
                NavigationHost(
                    navController = navController,
                    locationViewModel = locationViewModel,
                    currentWeatherViewModel = currentWeatherViewModel,
                    nextFiveDaysWeatherViewModel = nextFiveDaysWeatherViewModel,
                    navigationViewModel = navigationViewModel
                )
                if (currentScreen == NavigationRouter.HomeScreen) { // the color of the snack bar id very dark
                    LaunchedEffect(Unit) {
                        currentWeatherViewModel.message.collect { message ->
                            snackBarHostState.showSnackbar(
                                message = message,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                } else if(currentScreen == NavigationRouter.FavoriteScreen) {
                    /*LaunchedEffect(Unit) {
                        currentWeatherViewModel.message.collect { message -> // will need to sue the viewmodel of the favorite screen instead
                            snackBarHostState.showSnackbar(
                                message = message,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }*/
                }

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
    currentWeatherViewModel: CurrentWeatherViewModel,
    nextFiveDaysWeatherViewModel: NextFiveDaysWeatherViewModel,
    locationViewModel: LocationViewModel,
    navigationViewModel: NavigationViewModel
) {
    val currentLocation by locationViewModel.currentLocation.collectAsStateWithLifecycle()
    //val currentWeatherState by currentWeatherViewModel.currentWeatherState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController as NavHostController,
        startDestination = NavigationRouter.HomeScreen
    ) {
        composable<NavigationRouter.HomeScreen> {
            HomeScreen(
                location = currentLocation,
                //currentWeatherResponse = currentWeatherState,
                currentWeatherViewModel = currentWeatherViewModel,
                nextFiveDaysWeatherViewModel = nextFiveDaysWeatherViewModel,
                navigationViewModel = navigationViewModel
            )
        }
        composable<NavigationRouter.FavoriteScreen> { FavoriteScreen(navigationViewModel = navigationViewModel) }
        composable<NavigationRouter.AlertsScreen> { AlertsScreen(navigationViewModel = navigationViewModel) }
        composable<NavigationRouter.SettingAlertScreen> {
            SettingAlertScreen(
                locationViewModel = locationViewModel,
                navigationViewModel = navigationViewModel
            )
        }
        composable<NavigationRouter.SettingsScreen> { SettingsScreen(navigationViewModel = navigationViewModel) { } }
    }
}

@Composable
fun FloatingActionButtonUI(
    /*navController: NavController,
    navigationViewModel: NavigationViewModel*/
    onFABClick: () -> Unit
) {
    // val currentScreen = navigationViewModel.currentScreen.observeAsState()
    FloatingActionButton(
        onClick = {
            onFABClick()
            /* {
                 when (currentScreen.value) {
                     NavigationRouter.FavoriteScreen -> {} // show map to choose favorite place
                     else ->
                     navController.navigate(NavigationRouter.SettingAlertScreen)
                 //Log.d("FAB", "FAB clicked, navigating to SettingAlertScreen")

                 }
             }*/
        },
        containerColor = MaterialTheme.colorScheme.secondary
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "Floating action button."
        )
    }
}


/*
@Composable
private fun DisplaySnackBarByScreen(viewmodel: ViewModel, message: String) {
    LaunchedEffect(Unit) {
        viewModel.message.collect { message ->
            snackBarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }
}*/
