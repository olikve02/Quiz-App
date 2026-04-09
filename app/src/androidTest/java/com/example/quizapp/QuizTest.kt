package com.example.quizapp

import android.content.Context
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.quizapp.activity.Quiz
import com.example.quizapp.data.AppDatabase
import com.example.quizapp.model.QuizImage
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuizTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<Quiz>()

    @Before
    fun seedDb(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dao = AppDatabase.getDatabase(context).quizImageDao()

        runBlocking {
            dao.deleteAll()
            dao.insert(QuizImage(name = "A", imageUri = "android.resource://${context.packageName}/${R.drawable.gigachad}"))
            dao.insert(QuizImage(name = "B", imageUri = "android.resource://${context.packageName}/${com.example.quizapp.R.drawable.slay}"))
            dao.insert(QuizImage(name = "C", imageUri = "android.resource://${context.packageName}/${com.example.quizapp.R.drawable.gutta}"))
        }
    }

    @Test
    fun score_updates_after_wrong_and_right_answer(){
        composeRule.onNodeWithTag("score_text").assertTextEquals("Current Score is 0 / 0")

        composeRule.onAllNodesWithTag("wrong_answer")[0].performClick()
        composeRule.onNodeWithTag("score_text").assertTextEquals("Current Score is 0 / 1")

        composeRule.onNodeWithTag("correct_answer").performClick()
        composeRule.onNodeWithTag("score_text").assertTextEquals("Current Score is 1 / 2")
    }

}