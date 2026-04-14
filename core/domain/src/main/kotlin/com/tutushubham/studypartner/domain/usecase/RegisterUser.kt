package com.tutushubham.studypartner.domain.usecase

import com.tutushubham.studypartner.domain.repository.AuthRepository

class RegisterUser(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) = authRepository.register(email, password)
}
