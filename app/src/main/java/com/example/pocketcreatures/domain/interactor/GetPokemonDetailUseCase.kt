package com.example.pocketcreatures.domain.interactor

import com.example.pocketcreatures.domain.model.PokemonDetailResponse
import com.example.pocketcreatures.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonDetailUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(id:Int): Flow<Result<PokemonDetailResponse>> {
        return pokemonRepository.getPokemonDetail(id)
    }
}


