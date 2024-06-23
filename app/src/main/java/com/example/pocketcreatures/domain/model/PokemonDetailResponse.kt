package com.example.pocketcreatures.domain.model

data class PokemonDetailResponse(
    val abilities: List<Abilities>,
    val stats: List<Stat>,
    val species: NameAndUrl,
    val types:List<Type>,
    val height: Int,
    val weight: Int
)