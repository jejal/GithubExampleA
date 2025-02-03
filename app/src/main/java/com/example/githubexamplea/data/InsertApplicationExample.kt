package com.example.githubexamplea.data

import android.database.sqlite.SQLiteDatabase
import com.example.githubexamplea.dao.ApplicationDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InsertApplicationExample(private val db: SQLiteDatabase) {

    suspend fun insertSampleApplications() {
        val applicationDao = ApplicationDao(db)

        val applications = listOf(
            ApplicationData(
                userId = "lee",
                clubName = "Kicks & Dreams",
                date = "2025.02.08",
                time = "AM 09:00 초급자반"
            ),
            ApplicationData(
                userId = "lee",
                clubName = "Hit & Hustle",
                date = "2025.05.09",
                time = "PM 03:00 초급자반"
            ),
            ApplicationData(
                userId = "lee",
                clubName = "High Runner",
                date = "2025.03.10",
                time = "PM 01:00 초급자반"
            ),
            ApplicationData(
                userId = "lee",
                clubName = "Climb City",
                date = "2025.08.11",
                time = "PM 05:00 초급자반"
            ),
            ApplicationData(
                userId = "lee",
                clubName = "Dolphin",
                date = "2025.06.12",
                time = "AM 11:00 초급자반"
            ),
            ApplicationData(
                userId = "lee",
                clubName = "Breathe",
                date = "2025.04.13",
                time = "AM 09:00 초급자반"
            ),
            ApplicationData(
                userId = "miso",
                clubName = "Ping & Pong",
                date = "2025.07.14",
                time = "PM 03:00 초급자반"
            ),
            ApplicationData(
                userId = "miso",
                clubName = "Swing",
                date = "2025.09.15",
                time = "PM 01:00 초급자반"
            ),
            ApplicationData(
                userId = "miso",
                clubName = "Basket Team",
                date = "2025.10.16",
                time = "AM 09:00 초급자반"
            ),
            ApplicationData(
                userId = "jun",
                clubName = "Kicks & Dreams",
                date = "2025.03.17",
                time = "PM 05:00 초급자반"
            ),
            ApplicationData(
                userId = "jun",
                clubName = "Run & Run",
                date = "2025.11.18",
                time = "PM 01:00 초급자반"
            ),
            ApplicationData(
                userId = "future",
                clubName = "Kicks & Dreams",
                date = "2025.05.19",
                time = "AM 11:00 초급자반"
            ),
            ApplicationData(
                userId = "future",
                clubName = "Hit & Hustle",
                date = "2025.12.20",
                time = "PM 03:00 초급자반"
            ),
            ApplicationData(
                userId = "future",
                clubName = "Run & Run",
                date = "2025.04.21",
                time = "AM 09:00 초급자반"
            ),
            ApplicationData(
                userId = "future",
                clubName = "High Runner",
                date = "2025.08.22",
                time = "PM 05:00 초급자반"
            ),
            ApplicationData(
                userId = "future",
                clubName = "Climb City",
                date = "2025.06.23",
                time = "PM 01:00 초급자반"
            ),
            ApplicationData(
                userId = "future",
                clubName = "Ping & Pong",
                date = "2025.07.24",
                time = "AM 09:00 초급자반"
            ),
            ApplicationData(
                userId = "future",
                clubName = "Swing",
                date = "2025.09.25",
                time = "AM 11:00 초급자반"
            ),
            ApplicationData(
                userId = "nare",
                clubName = "Kicks & Dreams",
                date = "2025.03.26",
                time = "AM 09:00 초급자반"
            ),
            ApplicationData(
                userId = "nare",
                clubName = "Hit & Hustle",
                date = "2025.10.27",
                time = "PM 05:00 초급자반"
            ),
            ApplicationData(
                userId = "nare",
                clubName = "Run & Run",
                date = "2025.12.28",
                time = "AM 11:00 초급자반"
            ),
            ApplicationData(
                userId = "nare",
                clubName = "High Runner",
                date = "2025.12.01",
                time = "PM 03:00 초급자반"
            ),
            ApplicationData(
                userId = "nare",
                clubName = "Climb City",
                date = "2025.12.02",
                time = "PM 01:00 초급자반"
            ),
            ApplicationData(
                userId = "uuu",
                clubName = "Kicks & Dreams",
                date = "2025.12.03",
                time = "PM 03:00 초급자반"
            ),
            ApplicationData(
                userId = "uuu",
                clubName = "Hit & Hustle",
                date = "2025.12.04",
                time = "PM 05:00 초급자반"
            ),
            ApplicationData(
                userId = "uuu",
                clubName = "Ping & Pong",
                date = "2025.12.05",
                time = "PM 01:00 초급자반"
            ),
            ApplicationData(
                userId = "uuu",
                clubName = "Basket Team",
                date = "2025.12.06",
                time = "AM 09:00 초급자반"
            )
        )

        withContext(Dispatchers.IO) {
            for (app in applications) {
                applicationDao.insertApplication(
                    app.userId,
                    app.clubName,
                    app.date,
                    app.time
                )
            }
        }
    }

    data class ApplicationData(
        val userId: String,
        val clubName: String,
        val date: String,
        val time: String
    )
}
