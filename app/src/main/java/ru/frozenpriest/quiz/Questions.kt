package ru.frozenpriest.quiz

object Questions {

    private val questions = listOf(
        Question(question = "Test question?", answers = listOf("Yes", "No", "who knows", "add"), rightAnswerIndex = 0),
        Question(question = "Test question2?", answers = listOf("Yes", "No", "who knows", "add"), rightAnswerIndex = 0),
        Question(question = "Test question3?", answers = listOf("Yes", "No", "who knows", "add"), rightAnswerIndex = 0),
        Question(question = "Test question4?", answers = listOf("Yes", "No", "who knows", "add"), rightAnswerIndex = 0)
    )

    fun getQuestions(amount : Int) : List<Question> {
        return ShuffleUtils.pickNRandomElements(questions, amount)
    }
}