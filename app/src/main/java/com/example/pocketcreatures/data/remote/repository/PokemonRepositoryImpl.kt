package com.example.pocketcreatures.data.remote.repository

import com.example.pocketcreatures.data.ApiCall
import com.example.pocketcreatures.data.remote.api.PokemonApiService
import com.example.pocketcreatures.data.remote.model.asDomain
import com.example.pocketcreatures.domain.model.PokemonDetailResponse
import com.example.pocketcreatures.domain.model.PokemonResponse
import com.example.pocketcreatures.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: PokemonApiService,
) : PokemonRepository,ApiCall() {

    override suspend fun getPokemon(offset: Int, limit: Int): Flow<Result<PokemonResponse>> {
        return handleApi {
            apiService.getPokemon(limit, offset)
        }.map{ res ->
            res.mapToDomain { it.asDomain() }
        }
    }

    override suspend fun getPokemonDetail(id: Int): Flow<Result<PokemonDetailResponse>> {
        return handleApi {
            apiService.getSinglePokemon(id)
        }.map { res ->
            res.mapToDomain { it.asDomain() }
        }
    }

    private inline fun <T, R> Result<T>.mapToDomain(transform: (T) -> R): Result<R> {
        return fold(
            onSuccess = { Result.success(transform(it)) },
            onFailure = { Result.failure(it) }
        )
    }

}
