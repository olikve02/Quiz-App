package com.example.quizapp

import android.net.Uri
import android.net.Uri.parse
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.quizapp.model.QuizImage
import com.example.quizapp.model.QuizImageStore
import com.example.quizapp.ui.theme.QuizAppTheme
import kotlin.math.log

class Quiz : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShowQuiz(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ShowQuiz(modifier: Modifier = Modifier) {

    if (QuizImageStore.items.isEmpty()) {
        QuizImageStore.add(QuizImage(R.drawable.gigachad, "Giga Chad"))
        QuizImageStore.add(QuizImage(R.drawable.slay, "Slay"))
        QuizImageStore.add(QuizImage(R.drawable.gutta, "Vibes"))
    }

    var totalGuesses by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }

    // --- ROUND STATE ---
    var currentIndex by remember { mutableIntStateOf((0 until QuizImageStore.getSize()).random()) }
    val randomImage = QuizImageStore.getAtIndex(currentIndex)

    fun buildAnswers(forImage: QuizImage): List<String> {
        val wrongNames = QuizImageStore.items
            .filter { it != forImage }
            .map { it.name }
            .distinct()
            .shuffled()
            .take(2)

        return (wrongNames + forImage.name).shuffled()
    }

    var answers by remember { mutableStateOf(buildAnswers(randomImage)) }

    fun newRound() {
        // pick a new index (optionally avoid repeating same)
        var next = (0 until QuizImageStore.getSize()).random()
        if (QuizImageStore.getSize() > 1) {
            while (next == currentIndex) next = (0 until QuizImageStore.getSize()).random()
        }
        currentIndex = next
        answers = buildAnswers(QuizImageStore.getAtIndex(currentIndex))
    }

    val correctAnswer = randomImage.name

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Quiz", fontSize = 50.sp, modifier = Modifier.padding(16.dp))

        when {
            randomImage.imageRes != null -> {
                Image(
                    painter = painterResource(randomImage.imageRes),
                    contentDescription = null,
                    modifier.size(400.dp).padding(top = 12.dp)
                )
            }
            randomImage.imageUri != null -> {
                AsyncImage(
                    model = randomImage.imageUri?.let(Uri::parse),
                    contentDescription = null,
                    modifier = Modifier.size(400.dp).padding(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        answers.forEach { answer ->
            Button(
                onClick = {
                    totalGuesses++
                    val isCorrect = answer == correctAnswer
                    if (isCorrect) {
                        score++
                        newRound() // ✅ start a new round only when correct
                    }
                },
                modifier = Modifier.width(150.dp)
            ) {
                Text(text = answer)
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Text(text = "Current Score is $score / $totalGuesses")
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    QuizAppTheme {
        ShowQuiz()
    }
}