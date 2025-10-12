package com.russia.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.russia.geoquiz.databinding.ActivityCheatBinding

private const val EXTRA_ANSWER_IS_TRUE = "com.russia.geoquiz.answer_is_true"
private const val EXTRA_CHEAT_COUNT = "com.russia.geoquiz.cheat_count"
const val EXTRA_ANSWER_SHOWN = "com.russia.geoquiz.answer_shown"
const val IS_ANSWER_SHOWN = "com.russia.geoquiz.is_answer_shown"


class CheatActivity : AppCompatActivity() {

    private var answersIsTrue = false
    private lateinit var binding: ActivityCheatBinding
    private var isAnswerShown = false
    private var cheatCount = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        answersIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        cheatCount = intent.getIntExtra(EXTRA_CHEAT_COUNT, 3)
        isAnswerShown = savedInstanceState?.getBoolean(IS_ANSWER_SHOWN) ?: false
        binding.showAnswerButton.setOnClickListener {
            val answerText = when {
                answersIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            binding.answerTextView.setText(answerText)
            isAnswerShown = true
            setAnswerShownResult(true)
            updateCheatCountToolTip(cheatCount - 1)
        }
        updateCheatCountToolTip(cheatCount)
        setAnswerShownResult(isAnswerShown)
    }

    private fun updateCheatCountToolTip(count: Int) {
        binding.cheatCount.text = getString(
            R.string.cheat_count,
            count.toString()
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_ANSWER_SHOWN, isAnswerShown)
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answersIsTrue: Boolean, cheatCount: Int) : Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answersIsTrue)
                putExtra(EXTRA_CHEAT_COUNT, cheatCount)
            }
        }
    }

}