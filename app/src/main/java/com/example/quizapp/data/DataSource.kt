package com.example.quizapp.data

import com.example.quizapp.R
import com.example.quizapp.model.QuizImage

object DataSource {
    val intitialImageList: List<QuizImage> = listOf(
        QuizImage(R.drawable.gigachad, R.string.gigachad),
        QuizImage(R.drawable.slay, R.string.slay),
        QuizImage(R.drawable.gutta, R.string.gutta)
    )
}