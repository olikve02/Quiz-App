package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapp.model.QuizImage
import com.example.quizapp.ui.theme.QuizAppTheme

class Gallery : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    QuizImageItem(QuizImage(R.drawable.gigachad, R.string.gigachad), modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun QuizImageItem(quizImage: QuizImage, modifier: Modifier = Modifier){
    Card(

    ) {
        Column(
            modifier = modifier
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(quizImage.imageRes),
                contentDescription = null,
                modifier.size(100.dp)
            )
            Text(
                text = stringResource(quizImage.name),
            )
        }
    }
    
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuizAppTheme {
        QuizImageItem(QuizImage(R.drawable.gigachad, R.string.gigachad))
    }
}