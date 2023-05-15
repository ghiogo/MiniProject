package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    var gameStarted: Boolean = false
    val initialCountDown: Long = 60000
    val countDownInterval: Long = 1000
    var currentScore: Int = 0
    lateinit var tapMeButton: Button
    lateinit var gameScoreTextView: TextView
    lateinit var timeLeftTextView: TextView
    lateinit var countDownTimer: CountDownTimer
    var timeLeftOnTimer: Long = 60000


    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
        private const val SCORE_KEY: String = "SCORE_KEY"
        private const val TIME_LEFT_KEY: String = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "In onCreate. Current value: $currentScore")

        if (savedInstanceState != null) {
            currentScore = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }
    }

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, currentScore)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        countDownTimer.cancel()
        Log.d(TAG, "onSaveInstanceState called")
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun resetGame() {
        currentScore = 0
        gameScoreTextView.text = getString(R.string.yourScore, currentScore)

        val initialTimeLeft: Long = initialCountDown / 1000
        timeLeftTextView.text = getString(R.string.timeLeft, initialTimeLeft)

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
            }

            override fun onFinish() {
                endGame()
            }
        }

        gameStarted = false
    }

    private fun restoreGame() {
        gameScoreTextView.text = getString(R.string.yourScore, currentScore)
        timeLeftTextView.text = getString(R.string.timeLeft, timeLeftOnTimer / 1000)

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
            }

            override fun onFinish() {
                endGame()
            }
        }

        countDownTimer.start()
        gameStarted = true
    }

    private fun incrementScore(){
        if (!gameStarted) startGame()
        currentScore++
        val newScore: String = getString(R.string.yourScore, currentScore)
        gameScoreTextView.text = newScore
    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    private fun endGame() {

        Toast.makeText(this, getString(R.string.gameOverMessage, currentScore), Toast.LENGTH_LONG).show()
        resetGame()
    }
}