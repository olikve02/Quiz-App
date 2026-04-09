package com.example.quizapp

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.quizapp.activity.Gallery
import com.example.quizapp.data.AppDatabase
import com.example.quizapp.model.QuizImage
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GalleryTest {

    @get:Rule
    val composeRule = createEmptyComposeRule()

    @Before
    fun setUp() {
        Intents.init()

        val context = ApplicationProvider.getApplicationContext<Context>()
        val dao = AppDatabase.getDatabase(context).quizImageDao()

        runBlocking {
            dao.deleteAll()
            dao.insert(
                QuizImage(
                    name = "One",
                    imageUri = "android.resource://${context.packageName}/${com.example.quizapp.R.drawable.gigachad}"
                )
            )
            dao.insert(
                QuizImage(
                    name = "Two",
                    imageUri = "android.resource://${context.packageName}/${com.example.quizapp.R.drawable.slay}"
                )
            )
        }

        ActivityScenario.launch(Gallery::class.java)
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun count_is_correct_after_add_and_delete() {
        composeRule.onNodeWithTag("image_count")
            .assertTextEquals("Count: 2")

        val context = ApplicationProvider.getApplicationContext<Context>()
        val fakeUri = Uri.parse(
            "android.resource://${context.packageName}/${com.example.quizapp.R.drawable.gutta}"
        )

        val resultData = Intent().apply {
            data = fakeUri
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)

        intending(hasAction(Intent.ACTION_OPEN_DOCUMENT)).respondWith(result)

        composeRule.onNodeWithTag("add_photo_button").performClick()
        composeRule.onNodeWithTag("name_input").performTextInput("Three")
        composeRule.onNodeWithTag("confirm_add_button").performClick()

        composeRule.onNodeWithTag("image_count")
            .assertTextEquals("Count: 3")

        composeRule.onNodeWithTag("image_item_Three").performClick()
        composeRule.onNodeWithTag("confirm_delete_button").performClick()

        composeRule.onNodeWithTag("image_count")
            .assertTextEquals("Count: 2")
    }
}