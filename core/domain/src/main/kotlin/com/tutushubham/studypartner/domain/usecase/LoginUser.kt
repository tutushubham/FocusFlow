package com.tutushubham.studypartner.domain.usecase

import com.tutushubham.studypartner.domain.repository.AuthRepository

class LoginUser(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) = authRepository.login(email, password)
}
