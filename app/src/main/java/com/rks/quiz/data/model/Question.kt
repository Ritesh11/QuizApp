package com.rks.quiz.data.model

data class Question(
    var question: String,
    var category: String,
    var answer: String, var choices: List<String>
)