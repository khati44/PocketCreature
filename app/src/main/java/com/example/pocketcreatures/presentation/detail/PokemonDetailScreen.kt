package com.example.pocketcreatures.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pocketcreatures.domain.model.PokemonDetailResponse
import com.example.pocketcreatures.presentation.views.FullScreenImage

@Composable
fun PokemonDetailScreenWithViewModel(
    viewModel: PokemonDetailViewModel = hiltViewModel(),
    id:Int,
    onGoBack: () -> Unit,
    picUrl:String,
    name:String
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(id) {
        viewModel.getDetailedPokemon(id)
    }
    SinglePokemonDetailScreen(uiState,onGoBack,picUrl,name)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SinglePokemonDetailScreen(
    uiState: PokemonDetailUiState,
    onGoBack: () -> Unit,
    picUrl: String,
    name: String,
) {
    var showFullScreenImage by remember { mutableStateOf(false) }
    var fullScreenImageUrl by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(name) },
                navigationIcon = {
                    IconButton(onClick = onGoBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(it),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = picUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .height(250.dp)
                        .width(250.dp)
                        .clickable {
                            fullScreenImageUrl = picUrl
                            showFullScreenImage = true
                        },
                    contentScale = ContentScale.FillWidth
                )
            }
            when (uiState) {
                is PokemonDetailUiState.Loading -> {
                    CircularProgressIndicator()
                }
                
                is PokemonDetailUiState.Success -> {
                    PokemonInfoScreen(uiState.data)
                }
                is PokemonDetailUiState.Error -> {
                    Text(text = "something went wrong")
                }
            }
            if (showFullScreenImage) {
                FullScreenImage(
                    imageUrl = fullScreenImageUrl,
                    onDismiss = { showFullScreenImage = false }
                )
            }
        }
    }
}

@Composable
fun PokemonInfoScreen(data: PokemonDetailResponse) {

    Text(
        text = "type: " + data.types.firstOrNull()?.type?.name.orEmpty(),
        modifier = Modifier.padding(start = 16.dp, top = 24.dp),
        fontSize = 20.sp
    )
    if (data.abilities.isNotEmpty()){
        Text(
            text = "abilities",
            modifier = Modifier.padding(start = 16.dp, top = 24.dp),
            fontSize = 20.sp
        )
    }

    val abilities = data.abilities
    val stats = data.stats
    LazyColumn(modifier = Modifier
        .padding(16.dp)
        .height(100.dp)) {
        items(
            count = abilities.size,
            key = {
                abilities[it].ability.name.toString()
            },
            itemContent =  { index ->
                val ability = abilities[index]
                Text(text = ability.ability.name.orEmpty())
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 30.dp)
            .height(300.dp)
    ) {
        items(
            count = stats.size,
            key = {
                stats[it].stat.name.toString()
            },
            itemContent =  { index ->

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    val stat = stats[index]
                    Text(text = stat.stat.name.orEmpty().plus(" ").plus(stat.baseStat))
                    stat.baseStat.let { ProgressLine(it) }
                }
            }
        )
    }

}

@Composable
fun ProgressLine(progress: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(5.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(progress.toFloat() / 100)
                    .fillMaxHeight()
                    .background(Color.White)
            )
            Box(
                modifier = Modifier
                    .weight((100 - progress).toFloat() / 100)
                    .fillMaxHeight()
                    .background(Color.Gray)
            )
        }
    }
}