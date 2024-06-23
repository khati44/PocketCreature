package com.example.pocketcreatures.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonResponseDTO(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonResultDTO?>
) : Parcelable