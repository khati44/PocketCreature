package com.example.pocketcreatures.data.remote.model

import com.example.pocketcreatures.domain.model.PokemonDetailResponse


data class PokemonDetailResponseDTO(
    val abilities: List<AbilitiesDTO>,
    val stats: List<StatDTO>,
    val species:NameAndUrlDTO,
    val types:List<TypeDTO>,
    val height: Int,
    val weight: Int
)


fun PokemonDetailResponseDTO.asDomain() = PokemonDetailResponse(
    abilities = abilities.asDomain(),
    stats = stats.asDomain(),
    species = species.asDomain(),
    types = types.asDomain(),
    height = height,
    weight = weight
)

