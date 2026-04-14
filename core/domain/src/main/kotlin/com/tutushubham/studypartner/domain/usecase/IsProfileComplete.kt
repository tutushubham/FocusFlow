package com.tutushubham.studypartner.domain.usecase

import com.tutushubham.studypartner.domain.repository.UserRepository

class IsProfileComplete(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Boolean = userRepository.isProfileComplete()
}
