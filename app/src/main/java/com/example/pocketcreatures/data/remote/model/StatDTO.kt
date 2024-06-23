package com.example.pocketcreatures.data.remote.model

import com.example.pocketcreatures.domain.model.Stat

data class StatDTO(
    val base_stat:Int?,
    val stat: NameAndUrlDTO,
)

fun StatDTO.asDomain() = Stat(
    baseStat = base_stat ?: 0,
    stat = stat.asDomain()
)

fun List<StatDTO>.asDomain(): List<Stat> {
    return map { it.asDomain() }
}
