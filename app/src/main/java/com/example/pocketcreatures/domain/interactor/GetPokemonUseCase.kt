package com.example.pocketcreatures.domain.interactor

import com.example.pocketcreatures.domain.model.PokemonResponse
import com.example.pocketcreatures.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(offset: Int, limit: Int): Flow<Result<PokemonResponse>> {
        return pokemonRepository.getPokemon(offset, limit)
    }
}

