package ru.frozenpriest.quiz.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_question.*
import ru.frozenpriest.quiz.Question
import ru.frozenpriest.quiz.R


class QuestionActivity : AppCompatActivity() {

    private var factor : Double = 0.0

    private lateinit var question:Question
    private lateinit var questions:ArrayList<Question>
    private var questionNumber: Int = -1
    private lateinit var timer : CountDownTimer
    private var timerLength : Int = 0
    private var score = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_question)

        val extras = intent.extras
        if(extras != null) {
            questions = extras.getParcelableArrayList<Question>("questions")?:throw NullPointerException("Question should not be null")
        }


        //timer setup
        timerLength = resources.getInteger(R.integer.countdownTime)
        factor = 100.0 / timerLength

        openNextQuestion()
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
        setClickListeners(true)
        setupButtonListenersForAnswer()
        textViewQuestion.setOnClickListener(null)
        startTimer()
    }

    private fun setupButtonListenersForAnswer() {
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
        } else {
            score++
        }
        getButton(question.rightAnswerIndex).setBackgroundColor(ContextCompat.getColor(this, R.color.colorRightAnswer))

        setClickListeners(false)
        stopTimer()

        constraintLayout.setOnClickListener{openNextQuestion()}
    }

    private fun openNextQuestion() {
        if(questionNumber+1 >= questions.size) {
            val intent = Intent(this@QuestionActivity, ResultActivity::class.java)
            intent.putExtra("score", score)
            intent.putExtra("maxScore", questions.size)

            startActivity(intent)
            finish()
        } else {
            //open next question

            clearText()
            remakeOriginalListeners()

            question = questions[++questionNumber]


        }
    }

    private fun remakeOriginalListeners() {
        textViewQuestion.setOnClickListener {beginQuestion(it)}

        clearButtonColors()

        buttonAnswer1.setOnClickListener{}
        buttonAnswer2.setOnClickListener{}
        buttonAnswer3.setOnClickListener{}
        buttonAnswer4.setOnClickListener{}

    }

    private fun clearButtonColors() {
        buttonAnswer1.setBackgroundColor(ContextCompat.getColor(this, R.color.colorButtonBackground))
        buttonAnswer2.setBackgroundColor(ContextCompat.getColor(this, R.color.colorButtonBackground))
        buttonAnswer3.setBackgroundColor(ContextCompat.getColor(this, R.color.colorButtonBackground))
        buttonAnswer4.setBackgroundColor(ContextCompat.getColor(this, R.color.colorButtonBackground))
    }

    private fun clearText() {
        textViewQuestion.text = resources.getText(R.string.tap_to_begin)
        buttonAnswer1.text = ""
        buttonAnswer2.text = ""
        buttonAnswer3.text = ""
        buttonAnswer4.text = ""
    }

    private fun setClickListeners(clickable : Boolean) {
        textViewQuestion.isClickable = clickable

        buttonAnswer1.isClickable = clickable
        buttonAnswer2.isClickable = clickable
        buttonAnswer3.isClickable = clickable
        buttonAnswer4.isClickable = clickable

        buttonAnswer1.isEnabled = clickable
        buttonAnswer2.isEnabled = clickable
        buttonAnswer3.isEnabled = clickable
        buttonAnswer4.isEnabled = clickable
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
                openNextQuestion()
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