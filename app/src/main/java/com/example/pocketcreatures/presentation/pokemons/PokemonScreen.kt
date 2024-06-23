package com.example.pocketcreatures.presentation.pokemons

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pocketcreatures.data.remote.model.PokemonResultDTO
import com.example.pocketcreatures.utils.extractId
import com.example.pocketcreatures.utils.getPicUrl
import kotlinx.coroutines.flow.map


@Composable
fun PokemonScreenWithViewModel(
    viewModel: PokemonViewModel = hiltViewModel(),
    onShowDetails: (id: Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    PokemonScreen(state = uiState, onShowDetails = onShowDetails, onLoadMore = viewModel::onLoadMore)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonScreen(
    state: PokemonUiState,
    onShowDetails: (id: Int) -> Unit,
    onLoadMore: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pokemon List") }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    when (state) {
                        is PokemonUiState.NoData -> {
                            Text(text = "No pokemons to show :(")
                        }

                        is PokemonUiState.HasData -> {
                            PokemonList(
                                pokemonList = state.pokemonList,
                                onShowDetails = onShowDetails,
                                onLoadMore = onLoadMore,
                                isLoadingMore = state.isLoadingMore
                            )
                        }
                    }
                }
                ErrorMessages(errorMessages = state.errorMessages)
            }
        }
    )
}

@Composable
fun PokemonList(
    pokemonList: List<PokemonResultDTO?>,
    onShowDetails: (id: Int) -> Unit,
    onLoadMore: () -> Unit,
    isLoadingMore: Boolean
) {
    val listState = rememberLazyListState()
    Log.wtf("isLoadingMore", isLoadingMore.toString())
    LazyColumn(state = listState) {
        items(
            count = pokemonList.size,
            key = { pokemonList[it]?.name.toString() },
            itemContent = { index ->
                val pokemonData = pokemonList[index]
                if (pokemonData != null) {
                    PokemonItem(pokemon = pokemonData, onShowDetails = onShowDetails)
                }
            },
        )
        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(bottom = 100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .map { layoutInfo ->
                val visibleItemsInfo = layoutInfo.visibleItemsInfo
                if (visibleItemsInfo.isNotEmpty() && layoutInfo.totalItemsCount > 0) {
                    val lastVisibleItem = visibleItemsInfo.last()
                    val isCloseToEnd = lastVisibleItem.index >= layoutInfo.totalItemsCount - 4
                    isCloseToEnd
                } else {
                    false
                }
            }
            .collect { isCloseToEnd ->
                if (isCloseToEnd) {
                    onLoadMore()
                }
            }
    }
}

@Composable
fun PokemonItem(pokemon: PokemonResultDTO, onShowDetails: (id: Int) -> Unit) {
    val id = pokemon.url?.extractId() ?: 0
    val picUrl = pokemon.url?.getPicUrl() ?: ""
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = pokemon.name ?: "", modifier = Modifier.padding(8.dp))
        AsyncImage(
            model = picUrl,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clickable { onShowDetails(id) }
        )

        Button(onClick = { onShowDetails(id) }, modifier = Modifier.padding(8.dp)) {
            Text("Show Details")
        }
    }
}

@Composable
fun ErrorMessages(errorMessages: String) {
    if (errorMessages.isNotEmpty()) {
        Text(text = errorMessages, color = Color.Red)
    }
}