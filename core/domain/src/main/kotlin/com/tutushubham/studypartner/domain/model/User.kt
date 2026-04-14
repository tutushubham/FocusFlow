package com.tutushubham.studypartner.domain.model

import com.tutushubham.studypartner.model.StudyLevel
import com.tutushubham.studypartner.model.StudyTimeOfDay

data class SubjectTag(
    val id: String,
    val label: String,
)

data class User(
    val id: String,
    val name: String,
    val email: String,
    val photoUrl: String?,
    /** High-stakes exam focus (Stitch Profile Setup — Basic Info). */
    val examPreparingFor: String,
    val bio: String,
    val subjects: List<SubjectTag>,
    val studyTimePreferences: Set<com.tutushubham.studypartner.model.StudyTimeOfDay>,
    val dailyStudyGoalHours: Float,
    val experienceLevel: StudyLevel,
    val timezone: String,
    val sessionsCompleted: Int,
    val totalStudyHours: Float,
    val streakDays: Int,
)
