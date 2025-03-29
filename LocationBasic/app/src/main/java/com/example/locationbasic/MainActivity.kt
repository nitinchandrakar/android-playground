package com.example.locationbasic

import android.content.Context
import android.os.Bundle
import android.Manifest
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.locationbasic.ui.theme.LocationBasicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel:LocationViewModel = viewModel()
            LocationBasicTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun App(viewModel: LocationViewModel){
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)
    LocationDisplay(
        locationUtils = locationUtils,
        viewModel,
        context = context
    )
}

@Composable
fun LocationDisplay(
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    context: Context
){
    val location = viewModel.location.value

    val address = location?.let {
        locationUtils.reverseGeoCodeLocation(location)
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            // Handle permission result
            permissions ->
            if(permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true){
                // Permission granted, update the location

                locationUtils.requestLocationUpdates(viewModel = viewModel)
            }else{
                // Permission denied, handle accordingly
                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as ComponentActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context as ComponentActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )

                if(!rationaleRequired){
                    // Explain why the app needs the permission
                    Toast.makeText(
                        context,
                        "Location permission is required to display location",
                        Toast.LENGTH_LONG
                    ).show()
                } else{
                    // Permission denied, handle accordingly
                    Toast.makeText(
                        context,
                        "Location permission is required to display location, " +
                                "Please enable from settings",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    )

    Column (modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        if (location != null){
            Text("Latitude: ${location.latitude}")
            Text("Longitude: ${location.longitude}")
            Text("Address: $address")
        }else{
            Text(text = "Location Not Available")
        }

        Button(
            onClick = {
                if (locationUtils.hasLocationPermission(context)) {
                   // Permission already granted update the localtion
                    locationUtils.requestLocationUpdates(viewModel)
                } else {
                    // Request location permission
                    requestPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                }
            }
        ) {
            Text(text = "Get Location")
        }

    }
}