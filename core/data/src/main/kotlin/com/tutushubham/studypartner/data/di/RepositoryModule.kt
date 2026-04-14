package com.tutushubham.studypartner.data.di

import com.tutushubham.studypartner.data.repository.AuthRepositoryImpl
import com.tutushubham.studypartner.data.repository.ChatRepositoryImpl
import com.tutushubham.studypartner.data.repository.OnboardingRepositoryImpl
import com.tutushubham.studypartner.data.repository.SessionRepositoryImpl
import com.tutushubham.studypartner.data.repository.SettingsRepositoryImpl
import com.tutushubham.studypartner.data.repository.UserRepositoryImpl
import com.tutushubham.studypartner.domain.repository.AuthRepository
import com.tutushubham.studypartner.domain.repository.ChatRepository
import com.tutushubham.studypartner.domain.repository.OnboardingRepository
import com.tutushubham.studypartner.domain.repository.SessionRepository
import com.tutushubham.studypartner.domain.repository.SettingsRepository
import com.tutushubham.studypartner.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton abstract fun bindAuth(impl: AuthRepositoryImpl): AuthRepository
    @Binds @Singleton abstract fun bindUser(impl: UserRepositoryImpl): UserRepository
    @Binds @Singleton abstract fun bindOnboarding(impl: OnboardingRepositoryImpl): OnboardingRepository
    @Binds @Singleton abstract fun bindSettings(impl: SettingsRepositoryImpl): SettingsRepository
    @Binds @Singleton abstract fun bindSession(impl: SessionRepositoryImpl): SessionRepository
    @Binds @Singleton abstract fun bindChat(impl: ChatRepositoryImpl): ChatRepository
}
