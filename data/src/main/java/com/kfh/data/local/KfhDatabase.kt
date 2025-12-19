package com.kfh.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kfh.data.models.GithubUserDto

@Database(entities = [GithubUserDto::class], version = 1, exportSchema = false)
abstract class KfhDatabase: RoomDatabase() {

    companion object {
        const val DB_NAME = "kfh_database"
    }
}