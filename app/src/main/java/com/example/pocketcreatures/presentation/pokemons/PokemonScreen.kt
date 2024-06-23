package com.example.pocketcreatures.presentation.pokemons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pocketcreatures.domain.model.NameAndUrl
import com.example.pocketcreatures.presentation.views.FullScreenImage
import com.example.pocketcreatures.utils.extractId
import com.example.pocketcreatures.utils.getPicUrl
import kotlinx.coroutines.flow.map


@Composable
fun PokemonScreenWithViewModel(
    viewModel: PokemonViewModel = hiltViewModel(),
    onShowDetails: (id: Int,picUrl:String,name:String?) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    PokemonScreen(
        state = uiState,
        onShowDetails = onShowDetails,
        onLoadMore = viewModel::onLoadMore
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonScreen(
    state: PokemonUiState,
    onShowDetails: (id: Int,picUrl:String,name:String?) -> Unit,
    onLoadMore: () -> Unit
) {
    var showFullScreenImage by remember { mutableStateOf(false) }
    var fullScreenImageUrl by remember { mutableStateOf("") }

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
                                isLoadingMore = state.isLoadingMore,
                                onImageClick = {
                                    fullScreenImageUrl = it
                                    showFullScreenImage = true
                                }
                            )
                        }
                    }
                }
                ErrorMessages(errorMessages = state.errorMessages)
                if (showFullScreenImage) {
                    FullScreenImage(
                        imageUrl = fullScreenImageUrl,
                        onDismiss = { showFullScreenImage = false }
                    )
                }
            }
        }
    )
}


@Composable
fun PokemonList(
    pokemonList: List<NameAndUrl?>,
    onShowDetails: (id: Int,picUrl:String,name:String?) -> Unit,
    onLoadMore: () -> Unit,
    isLoadingMore: Boolean,
    onImageClick: (url: String) -> Unit
) {
    val listState = rememberLazyListState()
    LazyColumn(state = listState) {
        items(
            count = (pokemonList.size + 1) / 2,
            key = { pokemonList[it * 2]?.name.toString() },
            itemContent = { index ->
                val firstPokemon = pokemonList[index * 2]
                val secondPokemon = pokemonList.getOrNull(index * 2 + 1)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (firstPokemon != null) {
                        PokemonItem(
                            pokemon = firstPokemon,
                            onShowDetails = onShowDetails,
                            onImageClick = onImageClick
                        )
                    }
                    if (secondPokemon != null) {
                        PokemonItem(
                            pokemon = secondPokemon,
                            onShowDetails = onShowDetails,
                            onImageClick = onImageClick
                        )
                    }
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
fun PokemonItem(
    pokemon: NameAndUrl,
    onShowDetails: (id: Int,picUrl:String,name:String?) -> Unit,
    onImageClick: (url: String) -> Unit
) {
    val id = pokemon.url?.extractId() ?: 0
    val picUrl = pokemon.url?.getPicUrl() ?: ""
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = pokemon.name ?: "", modifier = Modifier.padding(8.dp))
        AsyncImage(
            model = picUrl,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clickable { onImageClick(picUrl) }
        )

        Button(onClick = { onShowDetails(id,picUrl,pokemon.name) }, modifier = Modifier.padding(8.dp)) {
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