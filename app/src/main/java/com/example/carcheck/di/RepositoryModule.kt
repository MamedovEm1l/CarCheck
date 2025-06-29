package com.example.carcheck.di

import com.example.carcheck.data.repository.CarRepository
import com.example.carcheck.data.repository.CarRepositoryImpl
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
    abstract fun bindCarRepository(
        repositoryImpl: CarRepositoryImpl
    ): CarRepository
}
