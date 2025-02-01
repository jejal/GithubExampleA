package com.example.githubexamplea.database

object DatabaseContract {
    const val DATABASE_NAME = "Actify.db"
    const val DATABASE_VERSION = 12

    object UserTable {
        const val TABLE_NAME = "tb_user"
        const val COLUMN_ID = "id"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_NAME = "name"
        const val COLUMN_BIRTHDAY = "birthday"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_UNIVERSITY = "university"
        const val COLUMN_MAJOR = "major"
    }

    object LeaderTable {
        const val TABLE_NAME = "tb_leader"
        const val COLUMN_CLUB_NAME = "club_name"
        const val COLUMN_ID = "id"
        const val COLUMN_LEADER_INTRO = "leader_introduction"
        const val COLUMN_CLUB_INTRO = "club_introduction"
        const val COLUMN_LEADER_CAREER = "leader_career"
        const val COLUMN_LEADER_PHOTO = "leader_photo_path"
    }

    object LikeTable {
        const val TABLE_NAME = "tb_like"
        const val COLUMN_ID = "id"
        const val COLUMN_CLUB_NAME = "club_name"
    }

    object ClubTable {
        const val TABLE_NAME = "tb_club"
        const val COLUMN_CLUB_NAME = "club_name"
        const val COLUMN_SHORT_TITLE = "short_title"
        const val COLUMN_SHORT_INTRODUCTION = "short_introduction"
        const val COLUMN_DATE = "date"
        const val COLUMN_TIME = "time"
        const val COLUMN_LOCATION = "location"
        const val COLUMN_NEEDS = "needs"
        const val COLUMN_COST = "cost"
        const val COLUMN_PHOTO_PATH = "photo_path"
    }

    object ApplicationTable {
        const val TABLE_NAME = "tb_application"
        const val COLUMN_ID = "id"
        const val COLUMN_CLUB_NAME = "club_name"
        const val COLUMN_DATE = "date"
        const val COLUMN_TIME = "time"
    }

    object ClubDetailsTable {
        const val TABLE_NAME = "tb_club_details"
        const val COLUMN_CLUB_NAME = "club_name"
        const val COLUMN_CLUB_INTRODUCTION = "club_introduction"
        const val COLUMN_PROGRAM_1 = "program_1"
        const val COLUMN_PROGRAM_2 = "program_2"
        const val COLUMN_PROGRAM_3 = "program_3"
    }

    object ReviewTable {
        const val TABLE_NAME = "tb_review"
        const val COLUMN_ID = "id"
        const val COLUMN_CLUB_NAME = "club_name"
        const val COLUMN_STAR = "star"
        const val COLUMN_REVIEW = "review"
        const val COLUMN_TIME = "time"
        const val COLUMN_DATE = "date"
    }

    object FaqTable {
        const val TABLE_NAME = "tb_faq"
        const val COLUMN_ID = "id"
        const val COLUMN_CLUB_NAME = "club_name"
        const val COLUMN_QUESTION = "question"
        const val COLUMN_ANSWER = "answer"
        const val COLUMN_TIME = "time"
        const val COLUMN_DATE = "date"
    }
}
