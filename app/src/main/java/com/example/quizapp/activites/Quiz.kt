package com.example.quizapp.activites

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.quizapp.R
import com.example.quizapp.data.AppDatabase
import com.example.quizapp.data.QuizImageDao
import com.example.quizapp.model.QuizImage
import com.example.quizapp.ui.theme.QuizAppTheme

class Quiz : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getDatabase(applicationContext)
        val dao = db.quizImageDao()

        setContent {
            QuizAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShowQuiz(
                        modifier = Modifier.padding(innerPadding),
                        dao = dao
                    )
                }
            }
        }
    }
}

@Composable
fun ShowQuiz(modifier: Modifier = Modifier, dao: QuizImageDao) {
    var images by remember { mutableStateOf<List<QuizImage>>(emptyList()) }

    var totalGuesses by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }

    var currentImage by remember { mutableStateOf<QuizImage?>(null) }
    var answers by remember { mutableStateOf<List<String>>(emptyList()) }

    val context = LocalContext.current

    fun buildAnswers(forImage: QuizImage, allImages: List<QuizImage>): List<String> {
        val wrongNames = allImages
            .filter { it.id != forImage.id }
            .map { it.name }
            .distinct()
            .shuffled()
            .take(2)

        return (wrongNames + forImage.name).shuffled()
    }

    fun newRound(allImages: List<QuizImage>) {
        if (allImages.isEmpty()) return

        val nextImage = if (allImages.size == 1) {
            allImages.first()
        } else {
            val available = allImages.filter { it.id != currentImage?.id }
            available.random()
        }

        currentImage = nextImage
        answers = buildAnswers(nextImage, allImages)
    }

    LaunchedEffect(Unit) {
        if(dao.getAll().isEmpty()){
            dao.insert(QuizImage(imageUri = "android.resource://${context.packageName}/${R.drawable.gigachad}", name = "Giga Chad"))
            dao.insert(QuizImage(imageUri = "android.resource://${context.packageName}/${R.drawable.slay}", name = "Slay"))
            dao.insert(QuizImage(imageUri = "android.resource://${context.packageName}/${R.drawable.gutta}", name = "Vibes"))
        }
        images = dao.getAll()

        if (images.isNotEmpty()) {
            newRound(images)
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Quiz", fontSize = 50.sp, modifier = Modifier.padding(16.dp))

        if (images.isEmpty()) {
            Text("No images found")
            return@Column
        }

        if (currentImage == null) {
            Text("Loading quiz...")
            return@Column
        }

        AsyncImage(
            model = currentImage!!.imageUri?.let(Uri::parse),
            contentDescription = null,
            modifier = Modifier
                .size(400.dp)
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        answers.forEach { answer ->
            val isCorrectAnswer = answer == currentImage!!.name

            Button(
                onClick = {
                    totalGuesses++

                    if (answer == currentImage!!.name) {
                        score++
                        newRound(images)
                    }
                },
                modifier = Modifier
                    .width(150.dp)
                    .testTag(if (isCorrectAnswer) "correct_answer" else "wrong_answer")
            ) {
                Text(text = answer)
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        Text(
            text = "Current Score is $score / $totalGuesses",
            modifier = Modifier.testTag("score_text")
            )
    }
}