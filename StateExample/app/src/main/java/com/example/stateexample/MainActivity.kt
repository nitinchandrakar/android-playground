package com.example.stateexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.stateexample.ui.theme.StateExampleTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StateExampleTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    CaptionGame()
                }
            }
        }
    }

    @Composable
    fun CaptionGame(){
        val treasureFound = remember { mutableStateOf(0) }

        val direction = remember { mutableStateOf("North") }

        Column{
            Text(text = "Treasure Found: ${treasureFound.value}")
            Text(text = "Current direction: ${direction.value}")

            Button(
                onClick = {
                    direction.value = "East"
                    if (Random.nextBoolean()) {
                        treasureFound.value++
                    }
                }
            ) {
                Text("Sail East")
            }

            Button(
                onClick = {
                    direction.value = "West"
                    if (Random.nextBoolean()) {
                        treasureFound.value++
                    }
                }
            ) {
                Text("Sail West")
            }

            Button(
                onClick = {
                    direction.value = "South"
                    if (Random.nextBoolean()) {
                        treasureFound.value++
                    }
                }
            ) {
                Text("Sail South")
            }

            Button(
                onClick = {
                    direction.value = "North"
                    if (Random.nextBoolean()) {
                        treasureFound.value++
                    }
                }
            ) {
                Text("Sail North")
            }
        }

    }
}
