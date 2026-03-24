package com.example.quizapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.quizapp.model.QuizImage

@Dao
interface QuizImageDao {
    @Query("SELECT * FROM QuizImage")
    suspend fun getAll(): List<QuizImage>

    @Query("DELETE from QuizImage")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(vararg quizImage: QuizImage)

    @Delete
    suspend fun delete(vararg quizImage: QuizImage)
}