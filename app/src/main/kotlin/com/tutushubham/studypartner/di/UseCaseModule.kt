package com.tutushubham.studypartner.di

import com.tutushubham.studypartner.domain.repository.UserRepository
import com.tutushubham.studypartner.domain.usecase.IsProfileComplete
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideIsProfileComplete(userRepository: UserRepository): IsProfileComplete = IsProfileComplete(userRepository)
}
