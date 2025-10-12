package com.russia.geoquiz

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.math.max
import kotlin.math.min

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    var countCheatUser = 3

    private val questionBank = listOf(
        Question(R.string.question_australia, true, 0),
        Question(R.string.question_oceans, true, 0),
        Question(R.string.question_mideast, false, 0),
        Question(R.string.question_africa, false, 0),
        Question(R.string.question_americas, true, 0),
        Question(R.string.question_asia, true, 0),
    )

    private var answers: MutableMap<Int, Boolean> = mutableMapOf()

    private var currentIndex: Int
        get() = savedStateHandle[CURRENT_INDEX_KEY] ?:0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    fun countingCheat(): Boolean {
        questionBank[currentIndex].cheatCount++
        countCheatUser--
        return countCheatUser > 0
    }

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    var isCheater : Boolean
        get() = savedStateHandle[IS_CHEATER_KEY] ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    fun moveToNext() {
        currentIndex =  (min(currentIndex + 1, questionBank.size - 1)) % questionBank.size
        isCheater = questionBank[currentIndex].cheatCount > 0
    }

    fun moveToBack() {
        currentIndex =  (max(currentIndex - 1, 0)) % questionBank.size
        isCheater = questionBank[currentIndex].cheatCount > 0
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }

    fun countAnswers(answer: Boolean) {
        answers.putIfAbsent(currentIndex, answer)
    }

    @SuppressLint("DefaultLocale")
    fun showStatistics(): String? {
        return if (answers.size == questionBank.size) {
            val correctAnswers = ((answers.values.count { it }.toDouble() / questionBank.size) * 100)
            String.format("%.2f", correctAnswers)
        } else null
    }

}