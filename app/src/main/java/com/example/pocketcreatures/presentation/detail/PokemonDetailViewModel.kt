package com.example.pocketcreatures.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketcreatures.common.Dispatchers
import com.example.pocketcreatures.domain.interactor.GetPokemonDetailUseCase
import com.example.pocketcreatures.domain.model.PokemonDetailResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val dispatchers:Dispatchers,
    private val detailPokemonUseCase: GetPokemonDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.Loading)
    val uiState: StateFlow<PokemonDetailUiState> = _uiState


    fun getDetailedPokemon(id: Int) {
        dispatchers.launchBackground(viewModelScope) {
            detailPokemonUseCase.invoke(id).collect { result ->
                result.fold(
                    onSuccess = { data -> _uiState.value = PokemonDetailUiState.Success(data) },
                    onFailure = { exception -> _uiState.value = PokemonDetailUiState.Error(exception.message ?: "Unknown error") }
                )
            }
        }
    }
}

sealed class PokemonDetailUiState {
    object Loading : PokemonDetailUiState()
    data class Success(val data: PokemonDetailResponse) : PokemonDetailUiState()
    data class Error(val message: String) : PokemonDetailUiState()
}