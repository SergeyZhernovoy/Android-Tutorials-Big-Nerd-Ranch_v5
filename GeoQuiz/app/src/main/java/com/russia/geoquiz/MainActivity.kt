package com.russia.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.russia.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater = result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            if (quizViewModel.isCheater) {
                val allCheatUse = quizViewModel.countingCheat()
                buttonCheatDisabled(allCheatUse)
            }
        }
    }

    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel : $quizViewModel")

        binding.apiVersionView.text = getString(
            R.string.api_version,
            Build.VERSION.SDK_INT.toString()
        )

        binding.trueButton.setOnClickListener {
            checkAnswer(true)
            buttonEnabledState(false)
        }
        binding.falseButton.setOnClickListener {
            checkAnswer(false)
            buttonEnabledState(false)
        }
        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            buttonEnabledState(true)
        }
        binding.backButton.setOnClickListener {
            quizViewModel.moveToBack()
            updateQuestion()
            buttonEnabledState(true)
        }
        binding.questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
        binding.cheatButton.setOnClickListener { view ->
            val answersIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answersIsTrue, quizViewModel.countCheatUser)
            cheatLauncher.launch(intent)
        }
        updateQuestion()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            blurCheatButton()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun blurCheatButton() {
        val effect = RenderEffect.createBlurEffect(10.0f, 10.0f, Shader.TileMode.CLAMP)
        binding.cheatButton.setRenderEffect(effect)
    }

    private fun buttonEnabledState(isEnabled: Boolean) {
        binding.trueButton.isEnabled = isEnabled
        binding.falseButton.isEnabled = isEnabled
    }

    private fun buttonCheatDisabled(isEnabled: Boolean) {
        binding.cheatButton.isEnabled = isEnabled
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
    }

    @SuppressLint("DefaultLocale")
    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        quizViewModel.countAnswers(userAnswer == correctAnswer)
        Snackbar.make(binding.root, messageResId, 2000).show()
        //Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        quizViewModel.showStatistics()?.let {
            Snackbar.make(binding.root, "Все ответы получены. Количество верных ответов $it %", 2000).show()
            //Toast.makeText(this, "Все ответы получены. Количество верных ответов $it %", Toast.LENGTH_SHORT).show()
        }

    }

}