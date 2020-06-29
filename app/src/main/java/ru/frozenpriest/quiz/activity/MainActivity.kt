package ru.frozenpriest.quiz.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.frozenpriest.quiz.Questions
import ru.frozenpriest.quiz.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun startQuiz(view: View) {
        if (textInputName.text.toString().isEmpty()) {
            Toast.makeText(this@MainActivity, "Please enter your name", Toast.LENGTH_SHORT)
                .show()
        } else {
            val intent = Intent(this@MainActivity, QuestionActivity::class.java)

            val arrayList = ArrayList(Questions.getQuestions(4))


            intent.putParcelableArrayListExtra("questions", arrayList)
            intent.putExtra("questionIndex", 0)


            startActivity(intent)
            finish()
        }

    }
}