package com.example.pocketcreatures.data.remote.model

import com.example.pocketcreatures.domain.model.Type

data class TypeDTO(
    val slot:Int,
    val type:NameAndUrlDTO
)

fun TypeDTO.asDomain() = Type(
    slot = slot,
    type = type.asDomain()
)

fun List<TypeDTO>.asDomain(): List<Type> {
    return map { it.asDomain() }
}