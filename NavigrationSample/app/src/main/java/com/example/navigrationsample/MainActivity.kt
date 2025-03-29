package com.example.navigrationsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.navigrationsample.ui.theme.NavigrationSampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigrationSampleTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "firstScreen"){
        composable("firstScreen"){
            FirstScreen{ name ->
                navController.navigate("secondScreen/$name")
            }
        }
        composable("secondScreen/{name}") {
            val name = it.arguments?.getString("name") ?: "no name"

            SecondScreen(name){
                navController.navigate("firstScreen")
            }
        }
    }
}