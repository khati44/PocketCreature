package com.example.pocketcreatures.data.remote.model

import com.example.pocketcreatures.domain.model.NameAndUrl

data class NameAndUrlDTO(
    val name:String?,
    val url:String?
)


fun NameAndUrlDTO.asDomain() = NameAndUrl(
    name = name ?: "Unknown",
    url = url ?: ""
)
