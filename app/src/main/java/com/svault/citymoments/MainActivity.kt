@file:OptIn(ExperimentalMaterial3Api::class)

package com.svault.citymoments

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.svault.citymoments.ui.theme.CityMomentsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CityMomentsTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
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
                    // redirect to add/edit moments screen
                }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Skill",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
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

        }
    }
}

@Composable
fun NewMomentScreen() {
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
        var location: String by remember { mutableStateOf("") }
        var note: String by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            Text("Location")
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                placeholder = { Text("e.g., Jetpack Compose", color = Color.LightGray) },
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
            Text("Dec 30, 2024 2:30 PM")
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                content = {
                    Text("Save Moment")
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue
                ), onClick = {

                })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CityMomentsTheme {
        NewMomentScreen()
    }
}