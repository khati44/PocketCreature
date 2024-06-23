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
            SharingStarted.Eagerly,
            _viewModelState.value.toUiState()
        )

    private val currentOffset = MutableStateFlow(0)
    private val limit = 10

    init {
        loadMorePokemon()
    }

    private fun loadMorePokemon() {
        if (_viewModelState.value.isLoadingMore) return

        _viewModelState.update { it.copy(isLoadingMore = true) }
        dispatchers.launchBackground(viewModelScope) {
            getPokemonUseCase.invoke(currentOffset.value, limit).collect { result ->
                result.fold(
                    onSuccess = { pokemonResponse ->
                        currentOffset.value += limit
                        _viewModelState.update { currentState ->
                            currentState.copy(
                                pokemonList = currentState.pokemonList.plus(pokemonResponse.results),
                                errorMessages = "",
                                isLoadingMore = false
                            )
                        }
                    },
                    onFailure = { throwable ->
                        _viewModelState.update { currentState ->
                            val errorMessages =
                                throwable.message + (throwable.cause?.message ?: "Unknown error")
                            currentState.copy(errorMessages = errorMessages, isLoadingMore = false)
                        }
                    }
                )
            }
        }
    }

    fun onLoadMore() {
        if (!_viewModelState.value.isLoadingMore) {
            loadMorePokemon()
        }
    }
}

sealed interface PokemonUiState {
    val isLoading: Boolean
    val isLoadingMore: Boolean
    val errorMessages: String

    data class NoData(
        override val isLoading: Boolean,
        override val isLoadingMore: Boolean,
        override val errorMessages: String
    ) : PokemonUiState

    data class HasData(
        val pokemonList: List<NameAndUrl?>,
        override val isLoading: Boolean,
        override val isLoadingMore: Boolean,
        override val errorMessages: String
    ) : PokemonUiState
}

private data class PokemonViewModelState(
    val pokemonList: List<NameAndUrl?> = emptyList(),
    val errorMessages: String = "",
    val isLoadingMore: Boolean = false
) {
    fun toUiState(): PokemonUiState = if (pokemonList.isEmpty()) {
        PokemonUiState.NoData(
            isLoading = pokemonList.isEmpty() && !isLoadingMore,
            isLoadingMore = isLoadingMore,
            errorMessages = errorMessages
        )
    } else {
        PokemonUiState.HasData(
            pokemonList = pokemonList,
            isLoading = pokemonList.isEmpty() && !isLoadingMore,
            isLoadingMore = isLoadingMore,
            errorMessages = errorMessages
        )
    }
}
