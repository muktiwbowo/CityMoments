@file:OptIn(ExperimentalMaterial3Api::class)

package com.svault.citymoments

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.sharp.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.svault.citymoments.ui.theme.CityMomentsTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    private val hasLocationPermission = mutableStateOf(false)

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                hasLocationPermission.value = true
            }

            permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                hasLocationPermission.value = true
            }

            else -> {
                hasLocationPermission.value = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hasLocationPermission.value = checkLocationPermission()
        enableEdgeToEdge()
        setContent {
            CityMomentsTheme {
                AppNavigation(
                    hasPermission = hasLocationPermission.value,
                    onRequestPermission = {
                        locationPermissionRequest.launch(
                            arrayOf(
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                )
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main_map"
    ) {
        composable(route = "main_map") {
            MainScreen(hasPermission, onNewMoment = {
                navController.navigate("new_moment")
            }, onRequestPermission = onRequestPermission)
        }
        composable(route = "new_moment") {
            NewMomentScreen(hasPermission, onSaveMoment = {
                navController.popBackStack()
            })
        }
        composable(route = "detail_moment") {
            DetailMomentScreen(onBackToMap = {

            })
        }
    }
}

@Composable
fun MainScreen(
    hasPermission: Boolean,
    onNewMoment: () -> Unit,
    onRequestPermission: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "CityMoments", color = Color.White)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = ShapeDefaults.ExtraLarge,
                containerColor = Color.Blue,
                onClick = {
                    if (hasPermission) {
                        onNewMoment()
                    } else {
                        onRequestPermission()
                    }
                }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Skill",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        if (hasPermission) {
            val yogyakarta = LatLng(-7.7956, 110.3695)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(yogyakarta, 12f)
            }
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                cameraPositionState = cameraPositionState
            ) {
                // show marker here
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Location permission is required")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onRequestPermission) {
                    Text("Grant Permission")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewMomentScreen(hasPermission: Boolean, onSaveMoment: () -> Unit) {
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var isLoadingLocation by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val currentDateTime = remember { LocalDateTime.now() }
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy · h:mm a")
    val formattedDateTime = currentDateTime.format(formatter)

    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            isLoadingLocation = true
            getCurrentLocation(context) { location ->
                currentLocation = location
                isLoadingLocation = false
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "New Moment", color = Color.White)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        onSaveMoment()
                    }) {
                        Icon(
                            Icons.Filled.Close, "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val latitude = currentLocation?.latitude
        val longitude = currentLocation?.longitude
        var note: String by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            Text("Location")
            OutlinedTextField(
                value = when {
                    isLoadingLocation -> "Getting your location..."
                    currentLocation != null -> "Lat: $latitude, Lng: $longitude"
                    else -> ""
                },
                onValueChange = { },
                placeholder = { Text("Lat: -7.7956, Lng: 110.3695", color = Color.LightGray) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Add a note")
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                placeholder = { Text("What happened here?", color = Color.LightGray) },
                singleLine = false,
                modifier = Modifier.fillMaxWidth(), minLines = 6
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(formattedDateTime)
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoadingLocation && currentLocation != null,
                content = {
                    Text("Save Moment")
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue
                ), onClick = {
                    // save to room database
                    onSaveMoment()
                })
        }
    }
}

@Composable
fun DetailMomentScreen(onBackToMap: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Detail Moment", color = Color.White)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        onBackToMap()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val yogyakarta = LatLng(-7.7956, 110.3695)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(yogyakarta, 12f)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(innerPadding),
                cameraPositionState = cameraPositionState
            ) {
            }
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(text = "Estuary Coffee", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Had an amazing manual brew here while working on my Android Revival Project. Perfect place to code!")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row {
                Icon(
                    Icons.Sharp.LocationOn, "Location",
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Yogyakarta, Indonesia", color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Icon(
                    Icons.Filled.DateRange, "Location",
                    tint = Color.DarkGray
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Dec 30, 2024 2:30 PM", color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                content = {
                    Text("Delete Moment", color = Color.Red)
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ), border = BorderStroke(1.dp, color = Color.Red),
                onClick = {
                    // delete moment
                    onBackToMap()
                })
        }
    }
}


@SuppressLint("MissingPermission")
private fun getCurrentLocation(
    context: Context,
    onLocation: (LatLng) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        CancellationTokenSource().token
    ).addOnSuccessListener { location ->
        location?.let {
            onLocation(LatLng(it.latitude, it.longitude))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CityMomentsTheme {
        AppNavigation(true, onRequestPermission = {})
    }
}