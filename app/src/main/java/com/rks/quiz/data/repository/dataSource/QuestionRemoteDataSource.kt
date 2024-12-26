package com.rks.quiz.data.repository.dataSource

import com.rks.quiz.data.model.Questions
import retrofit2.Response

interface QuestionRemoteDataSource {

    suspend fun getRemoteQuestions(): Response<Questions>
}