package com.example.pocketcreatures.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonResultDTO(
    val name: String?,
    val url: String?
) : Parcelable