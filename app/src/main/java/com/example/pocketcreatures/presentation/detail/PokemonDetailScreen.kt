package com.example.pocketcreatures.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pocketcreatures.presentation.pokemons.PokemonScreen
import com.example.pocketcreatures.presentation.pokemons.PokemonViewModel

@Composable
fun PokemonDetailScreenWithViewModel(
    viewModel: PokemonViewModel = hiltViewModel(),
    onShowDetails: (id: Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    PokemonScreen(
        state = uiState,
        onShowDetails = onShowDetails,
        onLoadMore = viewModel::onLoadMore
    )
}