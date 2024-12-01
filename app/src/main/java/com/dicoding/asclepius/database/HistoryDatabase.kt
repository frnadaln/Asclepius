package com.dicoding.asclepius.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao() : HistoryDao

    companion object {
        @Volatile
        private var instance : HistoryDatabase? = null
        fun getInstance(context: Context) : HistoryDatabase =
            instance ?: synchronized(this) {
                instance ?: databaseBuilder(
                    context.applicationContext,
                    HistoryDatabase::class.java,
                    "CancerHistory.db"
                ).build()
            }
    }
}
