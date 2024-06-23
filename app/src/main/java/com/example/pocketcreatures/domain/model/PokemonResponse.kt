package com.example.pocketcreatures.domain.model

data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results:List<NameAndUrl?>
)