package com.example.pocketcreatures.data.remote.repository

import com.example.pocketcreatures.data.ApiCall
import com.example.pocketcreatures.data.remote.api.PokemonApiService
import com.example.pocketcreatures.data.remote.model.PokemonResponseDTO
import com.example.pocketcreatures.domain.repository.PokemonRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: PokemonApiService,
) : PokemonRepository,ApiCall() {

    override suspend fun getPokemon(offset: Int, limit: Int): Flow<Result<PokemonResponseDTO>> {
        return handleApi {
            delay(3000)
            apiService.getPokemon(limit, offset)
        }
    }
}