package com.tutushubham.studypartner.data.local

import androidx.room.TypeConverter
import com.tutushubham.studypartner.model.MatchStatus
import com.tutushubham.studypartner.model.SessionRequestStatus
import com.tutushubham.studypartner.model.StudyLevel
import com.tutushubham.studypartner.model.StudyTimeOfDay

class Converters {
    @TypeConverter fun fromStudyLevel(v: StudyLevel): String = v.name
    @TypeConverter fun toStudyLevel(v: String): StudyLevel = StudyLevel.valueOf(v)

    @TypeConverter fun fromSessionStatus(v: SessionRequestStatus): String = v.name
    @TypeConverter fun toSessionStatus(v: String): SessionRequestStatus = SessionRequestStatus.valueOf(v)

    @TypeConverter fun fromMatchStatus(v: MatchStatus): String = v.name
    @TypeConverter fun toMatchStatus(v: String): MatchStatus = MatchStatus.valueOf(v)

    @TypeConverter fun fromTimeSet(values: Set<StudyTimeOfDay>): String = values.joinToString(",") { it.name }
    @TypeConverter fun toTimeSet(raw: String): Set<StudyTimeOfDay> =
        if (raw.isBlank()) emptySet() else raw.split(",").map { StudyTimeOfDay.valueOf(it) }.toSet()
}
