package com.johnmuindi.foreheadfrenzykenya.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johnmuindi.foreheadfrenzykenya.data.Category
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class RoundState { NOT_STARTED, COUNTDOWN, PLAYING, ENDED }

class GameViewModel : ViewModel() {

    var currentCategory: Category? = null
        private set

    private var wordQueue: MutableList<String> = mutableListOf()

    var currentWord by mutableStateOf("")
        private set

    var score by mutableIntStateOf(0)
        private set

    var skips by mutableIntStateOf(0)
        private set

    var timeLeft by mutableIntStateOf(60)
        private set

    var countdownValue by mutableIntStateOf(3)
        private set

    var roundState by mutableStateOf(RoundState.NOT_STARTED)
        private set

    // Track words for Results screen
    val correctWords = mutableStateListOf<String>()
    val skippedWords = mutableStateListOf<String>()

    private var timerJob: Job? = null

    fun startRound(category: Category, durationSeconds: Int = 60) {
        currentCategory = category
        wordQueue = category.words.shuffled().toMutableList()
        score = 0
        skips = 0
        timeLeft = durationSeconds
        correctWords.clear()
        skippedWords.clear()
        roundState = RoundState.COUNTDOWN
        nextWord()
        startCountdownThenTimer(durationSeconds)
    }

    private fun startCountdownThenTimer(durationSeconds: Int) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            for (i in 3 downTo 1) {
                countdownValue = i
                delay(1000L)
            }
            countdownValue = 0
            delay(700L)
            roundState = RoundState.PLAYING
            timeLeft = durationSeconds
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
            correctWords.add(currentWord)  // capture BEFORE nextWord()
            score++
            nextWord()
        }
    }

    fun markSkipped() {
        if (roundState == RoundState.PLAYING) {
            skippedWords.add(currentWord)  // capture BEFORE nextWord()
            skips++
            nextWord()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}