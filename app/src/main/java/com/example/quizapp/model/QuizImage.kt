package com.example.quizapp.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class QuizImage(
    @DrawableRes val imageRes: Int,
    @StringRes val name: Int
)
