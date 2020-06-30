package ru.frozenpriest.quiz.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_result.*
import ru.frozenpriest.quiz.R

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val score = intent.extras?.getInt("score")
        val maxScore = intent.extras?.getInt("maxScore")

        textViewScore.text = resources.getString(R.string.your_score_is, score, maxScore)
    }

    fun finishQuiz(view: View) {
        val intent = Intent(this@ResultActivity, MainActivity::class.java)

        startActivity(intent)
        finish()
    }
}