package com.example.quizapp.model

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class QuizImage(
    @DrawableRes val imageRes: Int? = null,
    val name: String,
    val imageUri: String? = null
)
