package com.example.quizapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quizapp.model.QuizImage

@Database(entities = [QuizImage::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun quizImageDao(): QuizImageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "quiz_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }

    }
}