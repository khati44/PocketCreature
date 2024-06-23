package com.example.pocketcreatures.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SinglePokemonResponse(
    val height: Int?,
    val weight: Int?
) : Parcelable