package com.rks.quiz.domain

import android.util.Log
import com.rks.quiz.data.model.DataOrException
import com.rks.quiz.data.model.Question
import com.rks.quiz.data.repository.dataSource.QuestionRemoteDataSource

class GetQuestionUseCase(private val dataSourceImpl: QuestionRemoteDataSource) {

    suspend fun execute(): DataOrException<ArrayList<Question>, Boolean, Exception> {
        var dataOrException = DataOrException<ArrayList<Question>, Boolean, Exception>()
        try {
            dataOrException.loading = true
            val response = dataSourceImpl.getRemoteQuestions()
            val body = response.body()

            if (body != null) {
                dataOrException.data = body
                dataOrException.loading = false
            }
        } catch (exp: Exception) {
            dataOrException.e = exp
            Log.d(GetQuestionUseCase::class.simpleName, "Exception: $exp")
        }

        return dataOrException;
    }
}
