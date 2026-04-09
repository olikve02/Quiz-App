package com.example.quizapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.R
import com.example.quizapp.data.AppDatabase
import com.example.quizapp.data.QuizImageRepo
import com.example.quizapp.model.QuizImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class QuizUiState(
    val images: List<QuizImage> = emptyList(),
    val totalGuesses: Int = 0,
    val score: Int = 0,
    val currentImage: QuizImage? = null,
    val answers: List<String> = emptyList(),
    val isLoading: Boolean = true,

)



class QuizViewModel(
    private val repository: QuizImageRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()
    private var hasLoadedData = false
    fun loadQuizData(defaultImages: List<QuizImage>) {
        if (hasLoadedData) return
        hasLoadedData = true

        viewModelScope.launch(Dispatchers.IO) {
            var images = repository.getAll()

            if (images.isEmpty()) {
                defaultImages.forEach { repository.insert(it) }
                images = repository.getAll()
            }

            if (images.isEmpty()) {
                _uiState.value = QuizUiState(
                    images = emptyList(),
                    isLoading = false
                )
            } else {
                val firstImage = images.random()
                val firstAnswers = buildAnswers(firstImage, images)

                _uiState.value = QuizUiState(
                    images = images,
                    currentImage = firstImage,
                    answers = firstAnswers,
                    isLoading = false
                )
            }
        }
    }

    private fun buildAnswers(forImage: QuizImage, allImages: List<QuizImage>): List<String> {
        val wrongNames = allImages
            .filter { it.id != forImage.id }
            .map { it.name }
            .distinct()
            .shuffled()
            .take(2)

        return (wrongNames + forImage.name).shuffled()
    }

    private fun newRound() {
        val state = _uiState.value
        val allImages = state.images
        val currentImage = state.currentImage

        if (allImages.isEmpty()) return

        val nextImage = if (allImages.size == 1) {
            allImages.first()
        } else {
            val available = allImages.filter { it.id != currentImage?.id }
            if (available.isEmpty()) allImages.random() else available.random()
        }

        _uiState.value = state.copy(
            currentImage = nextImage,
            answers = buildAnswers(nextImage, allImages)
        )
    }

    fun onAnswerClick(answer: String) {
        val state = _uiState.value
        val currentImage = state.currentImage ?: return

        val updatedGuesses = state.totalGuesses + 1
        val updatedScore = if (answer == currentImage.name) state.score + 1 else state.score

        _uiState.value = state.copy(
            totalGuesses = updatedGuesses,
            score = updatedScore
        )

        if (answer == currentImage.name) {
            newRound()
        }
    }
}