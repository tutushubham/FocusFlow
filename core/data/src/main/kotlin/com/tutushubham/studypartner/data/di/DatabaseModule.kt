package com.tutushubham.studypartner.data.di

import android.content.Context
import androidx.room.Room
import com.tutushubham.studypartner.data.local.FocusFlowDatabase
import com.tutushubham.studypartner.data.local.MessageDao
import com.tutushubham.studypartner.data.local.SessionDao
import com.tutushubham.studypartner.data.local.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): FocusFlowDatabase =
        Room.databaseBuilder(context, FocusFlowDatabase::class.java, "focus_flow.db")
            .fallbackToDestructiveMigration()
            .addCallback(
                object : androidx.room.RoomDatabase.Callback() {
                    override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                        val subjectsCs = """[{"id":"cs","label":"Computer Science"}]"""
                        val subjectsMath = """[{"id":"math","label":"Mathematics"}]"""
                        db.execSQL(
                            "INSERT INTO users (id,name,email,photoUrl,bio,subjectsJson,studyTimes,dailyStudyGoalHours,experienceLevel,timezone,sessionsCompleted,totalStudyHours,streakDays) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)",
                            arrayOf(
                                "owner-demo-1",
                                "Jordan",
                                "jordan@demo.local",
                                null,
                                "Deep work on algorithms and interview prep.",
                                subjectsCs,
                                "Evening",
                                2f,
                                "Intermediate",
                                "UTC",
                                8,
                                24f,
                                4,
                            ),
                        )
                        db.execSQL(
                            "INSERT INTO users (id,name,email,photoUrl,bio,subjectsJson,studyTimes,dailyStudyGoalHours,experienceLevel,timezone,sessionsCompleted,totalStudyHours,streakDays) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)",
                            arrayOf(
                                "owner-demo-2",
                                "Sam",
                                "sam@demo.local",
                                null,
                                "Calculus study blocks with whiteboard notes.",
                                subjectsMath,
                                "Morning",
                                1.5f,
                                "Beginner",
                                "UTC",
                                3,
                                6f,
                                1,
                            ),
                        )
                        val start = System.currentTimeMillis() + 3_600_000L
                        db.execSQL(
                            "INSERT INTO session_requests (id,ownerUserId,subjectId,subjectLabel,title,description,startEpochMillis,durationMinutes,level,status,isPublic) VALUES (?,?,?,?,?,?,?,?,?,?,?)",
                            arrayOf(
                                "seed-1",
                                "owner-demo-1",
                                "cs",
                                "Computer Science",
                                "Focus block",
                                "Algorithms",
                                start,
                                120,
                                "Intermediate",
                                "Posted",
                                1,
                            ),
                        )
                        db.execSQL(
                            "INSERT INTO session_requests (id,ownerUserId,subjectId,subjectLabel,title,description,startEpochMillis,durationMinutes,level,status,isPublic) VALUES (?,?,?,?,?,?,?,?,?,?,?)",
                            arrayOf(
                                "seed-2",
                                "owner-demo-2",
                                "math",
                                "Mathematics",
                                "Calculus",
                                "Chapter review",
                                start + 7_200_000L,
                                90,
                                "Beginner",
                                "Posted",
                                1,
                            ),
                        )
                    }
                },
            )
            .build()

    @Provides fun userDao(db: FocusFlowDatabase): UserDao = db.userDao()
    @Provides fun sessionDao(db: FocusFlowDatabase): SessionDao = db.sessionDao()
    @Provides fun messageDao(db: FocusFlowDatabase): MessageDao = db.messageDao()
}
