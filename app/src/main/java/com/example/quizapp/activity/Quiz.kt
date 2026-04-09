package com.example.quizapp.activites

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.quizapp.R
import com.example.quizapp.data.AppDatabase
import com.example.quizapp.data.QuizImageRepo
import com.example.quizapp.model.QuizImage
import com.example.quizapp.ui.theme.QuizAppTheme
import com.example.quizapp.viewmodel.QuizViewModel
import com.example.quizapp.viewmodel.QuizViewModelFactory
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

class Quiz : ComponentActivity() {

    private val viewModel: QuizViewModel by viewModels {
        val dao = AppDatabase.getDatabase(applicationContext).quizImageDao()
        val repository = QuizImageRepo(dao)
        QuizViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val defaultImages = listOf(
            QuizImage(
                imageUri = "android.resource://${packageName}/${R.drawable.gigachad}",
                name = "Giga Chad"
            ),
            QuizImage(
                imageUri = "android.resource://${packageName}/${R.drawable.slay}",
                name = "Slay"
            ),
            QuizImage(
                imageUri = "android.resource://${packageName}/${R.drawable.gutta}",
                name = "Vibes"
            )
        )

        setContent {
            QuizAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShowQuiz(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                        defaultImages = defaultImages
                    )
                }
            }
        }
    }


    @Composable
    fun ShowQuiz(
        modifier: Modifier = Modifier,
        viewModel: QuizViewModel,
        defaultImages: List<QuizImage>
    ) {
        val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

        LaunchedEffect(Unit) {
            viewModel.loadQuizData(defaultImages)
        }

        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Quiz", fontSize = 50.sp, modifier = Modifier.padding(16.dp))

            if (uiState.isLoading) {
                Text("Loading quiz...")
                return@Column
            }

            if (uiState.images.isEmpty()) {
                Text("No images found")
                return@Column
            }

            val currentImage = uiState.currentImage
            if (currentImage == null) {
                Text("Loading quiz...")
                return@Column
            }

            AsyncImage(
                model = currentImage.imageUri?.let(Uri::parse),
                contentDescription = null,
                modifier = Modifier
                    .size(400.dp)
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))

            uiState.answers.forEach { answer ->
                val isCorrectAnswer = answer == currentImage.name

                Button(
                    onClick = { viewModel.onAnswerClick(answer) },
                    modifier = Modifier
                        .width(150.dp)
                        .testTag(if (isCorrectAnswer) "correct_answer" else "wrong_answer")
                ) {
                    Text(text = answer)
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = "Current Score is ${uiState.score} / ${uiState.totalGuesses}",
                modifier = Modifier.testTag("score_text")
            )
        }
    }

}