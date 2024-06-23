package com.example.pocketcreatures.data.remote.model

import com.example.pocketcreatures.domain.model.Abilities

data class AbilitiesDTO(
    val ability:NameAndUrlDTO,
)

fun AbilitiesDTO.asDomain() = Abilities(
    ability = ability.asDomain()
)

fun List<AbilitiesDTO>.asDomain(): List<Abilities> {
    return map { it.asDomain() }
}