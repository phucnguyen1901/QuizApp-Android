package com.example.quizapp

data class QuizModel(
    var id: String,
    var name: String,
    var questionList: List<QuestionModel>
) {
    constructor() : this(id = "", name = "", questionList = emptyList())
}

data class QuestionModel(
    var question: String,
    var answers: List<String>,
    var correct: String
) {
    constructor() : this(question = "", answers = emptyList(), correct = "")
}