package com.example.pocketcreatures.data.remote.model

import com.example.pocketcreatures.domain.model.NameAndUrl
import com.example.pocketcreatures.domain.model.PokemonResponse

data class PokemonResponseDTO(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<NameAndUrlDTO?>
)

fun PokemonResponseDTO.asDomain() = PokemonResponse(
    count = count,
    next = next,
    previous = previous,
    results =  results.asDomain()
)

fun List<NameAndUrlDTO?>.asDomain(): List<NameAndUrl?> {
    return map { it?.asDomain() }
}