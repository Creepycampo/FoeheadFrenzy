package com.johnmuindi.foreheadfrenzykenya.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johnmuindi.foreheadfrenzykenya.data.Category
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class RoundState { NOT_STARTED, PLAYING, ENDED }

class GameViewModel : ViewModel() {

    var currentCategory: Category? = null
        private set

    private var wordQueue: MutableList<String> = mutableListOf()

    var currentWord by mutableStateOf("")
        private set

    var score by mutableStateOf(0)
        private set

    var skips by mutableStateOf(0)
        private set

    var timeLeft by mutableStateOf(60)
        private set

    var roundState by mutableStateOf(RoundState.NOT_STARTED)
        private set

    private var timerJob: Job? = null

    fun startRound(category: Category, durationSeconds: Int = 60) {
        currentCategory = category
        wordQueue = category.words.shuffled().toMutableList()
        score = 0
        skips = 0
        timeLeft = durationSeconds
        roundState = RoundState.PLAYING
        nextWord()
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            roundState = RoundState.ENDED
        }
    }

    fun nextWord() {
        if (wordQueue.isEmpty()) {
            wordQueue = currentCategory?.words?.shuffled()?.toMutableList() ?: mutableListOf()
        }
        currentWord = wordQueue.removeFirst()
    }

    fun markCorrect() {
        if (roundState == RoundState.PLAYING) {
            score++
            nextWord()
        }
    }

    fun markSkipped() {
        if (roundState == RoundState.PLAYING) {
            skips++
            nextWord()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}