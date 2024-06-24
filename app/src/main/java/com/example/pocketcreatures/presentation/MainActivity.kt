package com.example.pocketcreatures.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.pocketcreatures.presentation.detail.PokemonDetailScreenWithViewModel
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
                    popExitTransition = { ExitTransition.None }) {
                    composable<PokemonScreen> {
                        PokemonScreenWithViewModel(onShowDetails = { id, picUrl, name ->
                            navController.navigate(PokemonDetailScreen(id = id, picUrl = picUrl,name = name))
                        })
                    }
                    composable<PokemonDetailScreen> {
                        val detailScreen: PokemonDetailScreen = it.toRoute()
                        PokemonDetailScreenWithViewModel(
                            id = detailScreen.id,
                            picUrl = detailScreen.picUrl.toString(),
                            name = detailScreen.name ?: "Pokemon",
                            onGoBack = {
                                navController.popBackStack(
                                    navController.graph.startDestinationId,
                                    false
                                )
                            })

                        BackHandler {
                            navController.popBackStack(
                                navController.graph.startDestinationId,
                                false
                            )
                        }
                    }
                }
            }
        }
    }
}


@Serializable
object PokemonScreen

@Serializable
data class PokemonDetailScreen(
    val id: Int,
    val picUrl:String?,
    val name:String?
)