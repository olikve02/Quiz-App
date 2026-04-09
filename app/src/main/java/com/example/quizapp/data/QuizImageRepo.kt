package com.example.quizapp.data

import com.example.quizapp.model.QuizImage

class QuizImageRepo(private val dao: QuizImageDao) {
    suspend fun getAll(): List<QuizImage> {
        return dao.getAll()
    }
    suspend fun deleteAll() {
        dao.deleteAll()
    }

    suspend fun insert(item: QuizImage) {
        dao.insert(item)
    }
    suspend fun delete(item: QuizImage) {
        dao.delete(item)
    }
}