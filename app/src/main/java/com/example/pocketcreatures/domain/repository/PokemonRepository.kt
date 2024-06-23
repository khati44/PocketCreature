package com.example.pocketcreatures.domain.repository

import com.example.pocketcreatures.data.remote.model.PokemonResponseDTO
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun getPokemon(offset: Int, limit: Int): Flow<Result<PokemonResponseDTO>>
}