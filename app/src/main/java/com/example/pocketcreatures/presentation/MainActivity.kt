package com.example.pocketcreatures.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.pocketcreatures.presentation.pokemons.PokemonScreenWithViewModel
import com.example.pocketcreatures.presentation.theme.PocketCreaturesTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PocketCreaturesTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = PokemonScreen,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    popExitTransition = { ExitTransition.None }){
                    composable<PokemonScreen> {
                        PokemonScreenWithViewModel(onShowDetails = {
                            navController.navigate(PokemonDetailScreen(id = it.toString()))
                        })
                    }
                    composable<PokemonDetailScreen> {
                        val detailScreen:PokemonDetailScreen = it.toRoute()
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "${detailScreen.id}, years old")
                        }
                        BackHandler {

                            navController.popBackStack(navController.graph.startDestinationId, false)
                        }
                    }
                }
            }
        }
    }
}



@Serializable
sealed class Routes {

    @Serializable
    data object FirstScreen : Routes()

    @Serializable
    data class SecondScreen(val id:String) : Routes()

}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PocketCreaturesTheme {
        Greeting("Android")
    }
}

@Serializable
object PokemonScreen

@Serializable
data class PokemonDetailScreen(
    val id:String
)