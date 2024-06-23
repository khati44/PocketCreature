package com.example.pocketcreatures.di

import com.example.pocketcreatures.data.remote.repository.PokemonRepositoryImpl
import com.example.pocketcreatures.domain.repository.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsMovieRepository(
        pokemonRepositoryImpl: PokemonRepositoryImpl
    ): PokemonRepository
}