package com.rks.quiz.data.repository.dataSourceImpl

import com.rks.quiz.data.QuestionService
import com.rks.quiz.data.model.Questions
import com.rks.quiz.data.repository.dataSource.QuestionRemoteDataSource
import retrofit2.Response

class QuestionRemoteDataSourceImpl(private val service: QuestionService): QuestionRemoteDataSource {

    override suspend fun getRemoteQuestions(): Response<Questions> {
        return service.getQuestions()
    }
}