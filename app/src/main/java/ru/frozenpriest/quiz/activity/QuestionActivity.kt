package ru.frozenpriest.quiz.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_question.*
import ru.frozenpriest.quiz.Question
import ru.frozenpriest.quiz.R


class QuestionActivity : AppCompatActivity() {

    private var factor : Double = 0.0;

    private var rightAnswerIndex = -1
    private lateinit var timer : CountDownTimer;
    private var timerLength : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        val extras = intent.extras
        if(extras != null) {
            val index = extras.getInt("questionIndex")
            val question = extras.getParcelableArrayList<Question>("questions")?.get(index) ?: throw NullPointerException("Question should not be null")

            //todo shuffle answers

            textViewQuestion.text = question.question
            buttonAnswer1.text = question.answers[0]
            buttonAnswer2.text = question.answers[1]
            buttonAnswer3.text = question.answers[2]
            buttonAnswer4.text = question.answers[3]

            rightAnswerIndex = question.rightAnswerIndex
        }

        timerLength = resources.getInteger(R.integer.countdownTime)
        factor = 100.0 / timerLength

        startTimer()
    }

    fun answerQuestion(view: View) {
        val index : Int = when(view) {
            buttonAnswer1 -> 0
            buttonAnswer2 -> 1
            buttonAnswer3 -> 2
            buttonAnswer4 -> 3
            else -> -1
        }

        submitAnswer(index)
    }

    private fun submitAnswer(i: Int) {
        if(i != -1 && i != rightAnswerIndex) {
            getButton(i).setBackgroundColor(resources.getColor(R.color.colorWrongAnswer))
        }
        getButton(rightAnswerIndex).setBackgroundColor(resources.getColor(R.color.colorRightAnswer))

        disableButtonClickListeners()
        stopTimer()
    }

    private fun disableButtonClickListeners() {
        buttonAnswer1.isEnabled = false
        buttonAnswer2.isEnabled = false
        buttonAnswer3.isEnabled = false
        buttonAnswer4.isEnabled = false
    }

    private fun getButton(index: Int) : Button {
        when (index) {
            0 -> return buttonAnswer1
            1 -> return buttonAnswer2
            2 -> return buttonAnswer3
            3 -> return buttonAnswer4
        }
        throw IllegalArgumentException("Button index can be 0..3")
    }

    private fun startTimer() {
        Log.e("Timer","Starting timer")
        timer = object:CountDownTimer(timerLength.toLong(), 50) {
            override fun onFinish() {
                progressBar.progress = 0
                submitAnswer(-1)
            }

            override fun onTick(millisUntilFinished: Long) {
                val progressPercentage: Double = (timerLength - millisUntilFinished) * factor

                progressBar.progress = 100 - progressPercentage.toInt()
            }

        }
        timer.start()
    }

    private fun stopTimer() {
        timer.cancel()
    }

}