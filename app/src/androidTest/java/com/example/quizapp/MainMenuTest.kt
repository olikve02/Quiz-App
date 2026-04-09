package com.example.quizapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.quizapp.activity.Menu
import com.example.quizapp.activity.Quiz
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainMenuTest {
    //Turn on Espresso intents and start Menu activity
    @Before
    fun setUp(){
        Intents.init()
        ActivityScenario.launch(Menu::class.java)
    }
    //Turn off Espresso intents and clean up
    @After
    fun tearDown(){
        Intents.release()
    }
    //Run test clicking button and check if button works
    @Test
    fun clickingQuizButton_opensQuizActivity(){
        onView(withId(R.id.goToQuizBtn)).perform(click())
        intended(hasComponent(Quiz::class.java.name))
    }

}