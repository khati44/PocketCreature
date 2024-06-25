package com.example.pocketcreatures.presentation.pokemons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketcreatures.common.Dispatchers
import com.example.pocketcreatures.domain.interactor.GetPokemonUseCase
import com.example.pocketcreatures.domain.model.NameAndUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getPokemonUseCase: GetPokemonUseCase,
    private val dispatchers: Dispatchers,
) : ViewModel() {

    private val _viewModelState = MutableStateFlow(PokemonViewModelState())
    val uiState: StateFlow<PokemonUiState> = _viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _viewModelState.value.toUiState()
        )

    private val currentOffset = MutableStateFlow(0)
    private val limit = 10

    init {
        loadMorePokemon()
    }

    private fun loadMorePokemon() {
        if (_viewModelState.value.isLoading) return

        _viewModelState.update { it.copy(isLoading = true) }
        dispatchers.launchBackground(viewModelScope) {
            getPokemonUseCase.invoke(currentOffset.value, limit).collect { result ->
                result.fold(
                    onSuccess = { pokemonResponse ->
                        currentOffset.value += limit
                        _viewModelState.update { currentState ->
                            currentState.copy(
                                pokemonList = currentState.pokemonList.plus(pokemonResponse.results),
                                isError = false,
                                isLoading = false
                            )
                        }
                    },
                    onFailure = {
                        _viewModelState.update { currentState ->
                            currentState.copy(isError = true, isLoading = false)
                        }
                    }
                )
            }
        }
    }

    fun onLoadMore() {
        if (!_viewModelState.value.isLoading) {
            loadMorePokemon()
        }
    }
}

sealed interface PokemonUiState {
    val isLoading: Boolean
    val isLoadingMore: Boolean
    val isError: Boolean

    data class NoData(
        override val isLoading: Boolean,
        override val isLoadingMore: Boolean,
        override val isError: Boolean
    ) : PokemonUiState

    data class HasData(
        val pokemonList: List<NameAndUrl?>,
        override val isLoading: Boolean,
        override val isLoadingMore: Boolean,
        override val isError: Boolean
    ) : PokemonUiState
}

private data class PokemonViewModelState(
    val pokemonList: List<NameAndUrl?> = emptyList(),
    val isError: Boolean = false,
    val isLoading: Boolean = false
) {
    fun toUiState(): PokemonUiState = if (pokemonList.isEmpty()) {
        PokemonUiState.NoData(
            isLoading = pokemonList.isEmpty() && isLoading,
            isLoadingMore = isLoading,
            isError = isError
        )
    } else {
        PokemonUiState.HasData(
            pokemonList = pokemonList,
            isLoading = false,
            isLoadingMore = isLoading && pokemonList.isNotEmpty(),
            isError = isError
        )
    }
}
