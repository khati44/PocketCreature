package com.example.pocketcreatures.domain.repository

import com.example.pocketcreatures.domain.model.PokemonDetailResponse
import com.example.pocketcreatures.domain.model.PokemonResponse
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun getPokemon(offset: Int, limit: Int): Flow<Result<PokemonResponse>>
    suspend fun getPokemonDetail(id:Int): Flow<Result<PokemonDetailResponse>>
}