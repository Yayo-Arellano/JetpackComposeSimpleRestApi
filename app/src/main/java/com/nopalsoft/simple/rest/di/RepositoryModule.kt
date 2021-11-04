package com.nopalsoft.simple.rest.di

import com.nopalsoft.simple.rest.repository.UserRepository
import com.nopalsoft.simple.rest.repository.UserRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun userRepository(repo: UserRepositoryImp): UserRepository
}








