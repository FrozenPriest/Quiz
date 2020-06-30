package ru.frozenpriest.quiz.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_question.*
import ru.frozenpriest.quiz.Question
import ru.frozenpriest.quiz.R


class QuestionActivity : AppCompatActivity() {

    private var factor : Double = 0.0

    private lateinit var question:Question
    private var questionNumber: Int = 0
    private lateinit var timer : CountDownTimer
    private var timerLength : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_question)

        val extras = intent.extras
        if(extras != null) {
            questionNumber = extras.getInt("questionIndex")
            question = extras.getParcelableArrayList<Question>("questions")?.get(questionNumber) ?: throw NullPointerException("Question should not be null")

            //todo shuffle answers


        }

        timerLength = resources.getInteger(R.integer.countdownTime)
        factor = 100.0 / timerLength
    }

    private fun setupQuestionText() {
        textViewQuestion.text = question.question
        buttonAnswer1.text = question.answers[0]
        buttonAnswer2.text = question.answers[1]
        buttonAnswer3.text = question.answers[2]
        buttonAnswer4.text = question.answers[3]
    }

    fun beginQuestion(view: View) {
        setupQuestionText()
        setupButtonListeners()
        textViewQuestion.setOnClickListener(null)
        startTimer()
    }

    private fun setupButtonListeners() {
        buttonAnswer1.setOnClickListener {answerQuestion(it)}
        buttonAnswer2.setOnClickListener {answerQuestion(it)}
        buttonAnswer3.setOnClickListener {answerQuestion(it)}
        buttonAnswer4.setOnClickListener {answerQuestion(it)}
    }

    private fun answerQuestion(view: View) {
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
        if(i != -1 && i != question.rightAnswerIndex) {
            getButton(i).setBackgroundColor(ContextCompat.getColor(this, R.color.colorWrongAnswer))
        }
        getButton(question.rightAnswerIndex).setBackgroundColor(ContextCompat.getColor(this, R.color.colorRightAnswer))

        disableClickListeners()
        stopTimer()

        constraintLayout.setOnClickListener{view -> openNextQuestion(view)}
    }

    private fun openNextQuestion(view: View) {
        val arrayList = intent.extras?.getParcelableArrayList<Question>("questions")!!
        if(questionNumber+1 >= arrayList.size) {
            //todo open final score
            Toast.makeText(this, "Finished", Toast.LENGTH_LONG).show()
        } else {
            //open next question

            val newIntent = Intent(this@QuestionActivity, QuestionActivity::class.java)

            newIntent.putParcelableArrayListExtra("questions", arrayList)
            newIntent.putExtra("questionIndex", questionNumber+1)


            startActivity(newIntent)
            finish()
        }
    }

    private fun disableClickListeners() {
        textViewQuestion.isClickable = false

        buttonAnswer1.isClickable = false
        buttonAnswer2.isClickable = false
        buttonAnswer3.isClickable = false
        buttonAnswer4.isClickable = false

        buttonAnswer1.isEnabled = false
        buttonAnswer2.isEnabled = false
        buttonAnswer3.isEnabled = false
        buttonAnswer4.isEnabled = false
    }

    private fun getButton(index: Int) : TextView {
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