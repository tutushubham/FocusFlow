package com.tutushubham.studypartner.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tutushubham.studypartner.data.local.MatchEntity
import com.tutushubham.studypartner.data.local.MessageEntity
import com.tutushubham.studypartner.data.local.SessionRequestEntity
import com.tutushubham.studypartner.data.local.UserEntity
import com.tutushubham.studypartner.domain.model.Message
import com.tutushubham.studypartner.domain.model.StudySessionMatch
import com.tutushubham.studypartner.domain.model.StudySessionRequest
import com.tutushubham.studypartner.domain.model.SubjectTag
import com.tutushubham.studypartner.domain.model.User

private val gson = Gson()
private val subjectListType = object : TypeToken<List<SubjectTag>>() {}.type

fun UserEntity.toDomain(): User {
    val subjects: List<SubjectTag> = gson.fromJson(subjectsJson, subjectListType)
    return User(
        id = id,
        name = name,
        email = email,
        photoUrl = photoUrl,
        examPreparingFor = examPreparingFor,
        bio = bio,
        subjects = subjects,
        studyTimePreferences = studyTimes,
        dailyStudyGoalHours = dailyStudyGoalHours,
        experienceLevel = experienceLevel,
        timezone = timezone,
        sessionsCompleted = sessionsCompleted,
        totalStudyHours = totalStudyHours,
        streakDays = streakDays,
    )
}

fun User.toEntity(): UserEntity =
    UserEntity(
        id = id,
        name = name,
        email = email,
        photoUrl = photoUrl,
        examPreparingFor = examPreparingFor,
        bio = bio,
        subjectsJson = gson.toJson(subjects),
        studyTimes = studyTimePreferences,
        dailyStudyGoalHours = dailyStudyGoalHours,
        experienceLevel = experienceLevel,
        timezone = timezone,
        sessionsCompleted = sessionsCompleted,
        totalStudyHours = totalStudyHours,
        streakDays = streakDays,
    )

fun SessionRequestEntity.toDomain(): StudySessionRequest =
    StudySessionRequest(
        id = id,
        ownerUserId = ownerUserId,
        subject = SubjectTag(subjectId, subjectLabel),
        title = title,
        description = description,
        startEpochMillis = startEpochMillis,
        durationMinutes = durationMinutes,
        level = level,
        status = status,
        isPublic = isPublic,
    )

fun StudySessionRequest.toEntity(): SessionRequestEntity =
    SessionRequestEntity(
        id = id,
        ownerUserId = ownerUserId,
        subjectId = subject.id,
        subjectLabel = subject.label,
        title = title,
        description = description,
        startEpochMillis = startEpochMillis,
        durationMinutes = durationMinutes,
        level = level,
        status = status,
        isPublic = isPublic,
    )

fun MatchEntity.toDomain(): StudySessionMatch =
    StudySessionMatch(
        id = id,
        requestId = requestId,
        ownerUserId = ownerUserId,
        partnerUserId = partnerUserId,
        scheduledStartEpochMillis = scheduledStartEpochMillis,
        scheduledEndEpochMillis = scheduledEndEpochMillis,
        status = status,
        subjectLabel = subjectLabel,
    )

fun MessageEntity.toDomain(): Message =
    Message(
        id = id,
        sessionMatchId = sessionMatchId,
        senderUserId = senderUserId,
        text = text,
        sentAtEpochMillis = sentAtEpochMillis,
    )
