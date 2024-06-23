package com.example.pocketcreatures.data.remote.api

import com.example.pocketcreatures.data.BaseResponse
import com.example.pocketcreatures.data.remote.model.PokemonResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {

//    @GET(PATH_POKEMON)
//    suspend fun getPokemon(
//        @Query("limit") limit: Int?,
//        @Query("offset") offset: Int?
//    ): BaseResponse<PokemonResponseDTO>

    @GET(PATH_POKEMON)
    suspend fun getPokemon(
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?
    ): Response<PokemonResponseDTO>

    @GET("pokemon/{id}/")
    suspend fun getSinglePokemon(
        @Path("id") id: Int
    ): BaseResponse<Unit>

    companion object {
        private const val PATH_POKEMON_DETAIL = "pokemon/{id}/"
        private const val PATH_POKEMON = "v2/pokemon/"
    }
}