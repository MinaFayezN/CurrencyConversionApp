package dev.mina.currency.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mina.currency.converter.ConverterRepo
import dev.mina.currency.converter.ConverterRepoImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun converterRepo(repo: ConverterRepoImpl): ConverterRepo
}

