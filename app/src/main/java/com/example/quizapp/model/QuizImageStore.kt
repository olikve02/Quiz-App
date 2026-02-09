package com.example.quizapp.model

import androidx.compose.runtime.mutableStateListOf

object QuizImageStore {
    val items = mutableStateListOf<QuizImage>()

    fun add(item: QuizImage) = items.add(item)
    fun remove(item: QuizImage) = items.remove(item)
}