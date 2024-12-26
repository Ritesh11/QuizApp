package com.rks.quiz.data

import com.rks.quiz.data.model.Questions
import retrofit2.Response
import retrofit2.http.GET

interface QuestionService {

@GET("master/world.json")
suspend fun getQuestions(): Response<Questions>
}