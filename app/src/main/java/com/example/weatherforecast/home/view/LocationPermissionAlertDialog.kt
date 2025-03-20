package com.example.weatherforecast.home.view

import androidx.annotation.DrawableRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.weatherforecast.R


@Composable
fun LocationPermissionAlertDialog(onDismissRequest: () -> Unit, onConfirmation: () -> Unit) {
    //val openAlertDialog = remember { mutableStateOf(false) }
    //when {
       // openAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = { onDismissRequest() }, //
                onConfirmation = { onConfirmation() }, // access location
                dialogTitle = "Location Permission Request",
                dialogText = "Weather Forecast needs to access your location to be able to show you " +
                        "the weather. Please turn on your location." /*+
                        "PS: You can choose your location on the map by going to the application settings " +
                        "and choosing 'map' under location option."*/,
                icon = R.drawable.ic_location
            )
      //  }
//    }
}

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    @DrawableRes icon: Int,
) {
    AlertDialog(
        //modifier = Modifier.c,
        icon = {
            Icon(painterResource(icon), contentDescription = "Example Icon", tint = Color.Black)
        },
        title = {
            Text(text = dialogTitle, fontSize = 18.sp, fontWeight = FontWeight.Medium)
        },
        text = {
            Text(text = dialogText, fontSize = 16.sp,fontWeight = FontWeight.Normal)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(text = "Allow", fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = "Deny", fontSize = 16.sp, color = Color.Red, fontWeight = FontWeight.Normal)
            }
        }
    )
}

