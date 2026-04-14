package com.tutushubham.studypartner.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tutushubham.studypartner.model.StudyLevel
import com.tutushubham.studypartner.model.StudyTimeOfDay

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val photoUrl: String?,
    val bio: String,
    val subjectsJson: String,
    val studyTimes: Set<StudyTimeOfDay>,
    val dailyStudyGoalHours: Float,
    val experienceLevel: StudyLevel,
    val timezone: String,
    val sessionsCompleted: Int,
    val totalStudyHours: Float,
    val streakDays: Int,
)
