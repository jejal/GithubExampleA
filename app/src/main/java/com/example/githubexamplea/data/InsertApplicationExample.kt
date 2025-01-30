package com.example.githubexamplea.data

import android.content.Context
import com.example.githubexamplea.dao.ApplicationDao
import com.example.githubexamplea.database.DatabaseHelper

class InsertApplicationExample(private val context: Context) {

    fun insertSampleApplications() {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase
        val applicationDao = ApplicationDao(db)

        val applications = listOf(
            ApplicationData(
                userId = "miso",
                clubName = "Kicks & Dreams",
                date = "2023-10-05",
                time = "14:30"
            ),
            ApplicationData(
                userId = "jun",
                clubName = "Kicks & Dreams",
                date = "2023-10-06",
                time = "16:00"
            ),
            ApplicationData(
                userId = "nare",
                clubName = "Kicks & Dreams",
                date = "2023-10-07",
                time = "10:00"
            ),
            ApplicationData(
                userId = "future",
                clubName = "Kicks & Dreams",
                date = "2023-10-08",
                time = "15:30"
            ),
            ApplicationData(
                userId = "actify",
                clubName = "Hit & Hustle",
                date = "2023-10-05",
                time = "14:30"
            ),
            ApplicationData(
                userId = "miso",
                clubName = "Hit & Hustle",
                date = "2023-10-06",
                time = "16:00"
            ),
            ApplicationData(
                userId = "jun",
                clubName = "Hit & Hustle",
                date = "2023-10-07",
                time = "10:00"
            ),
            ApplicationData(
                userId = "future",
                clubName = "Hit & Hustle",
                date = "2023-10-08",
                time = "15:30"
            ),
            ApplicationData(
                userId = "nare",
                clubName = "Hit & Hustle",
                date = "2023-10-05",
                time = "14:30"
            ),
            ApplicationData(
                userId = "uuu",
                clubName = "Hit & Hustle",
                date = "2023-10-06",
                time = "16:00"
            ),
            ApplicationData(
                userId = "nare",
                clubName = "Hit & Hustle",
                date = "2023-10-07",
                time = "10:00"
            ),
            ApplicationData(
                userId = "nare",
                clubName = "Breathe",
                date = "2023-10-08",
                time = "15:30"
            ),
            ApplicationData(
                userId = "uuu",
                clubName = "Breathe",
                date = "2023-10-08",
                time = "15:30"
            )
        )

        db.beginTransaction()
        try {
            for (app in applications) {
                applicationDao.insertApplication(
                    app.userId,
                    app.clubName,
                    app.date,
                    app.time
                )
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    data class ApplicationData(
        val userId: String,
        val clubName: String,
        val date: String,
        val time: String
    )
}
