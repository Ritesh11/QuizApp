package com.rks.quiz.presenter.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rks.quiz.domain.GetQuestionUseCase

class QuizViewModelFactory(
    private val getQuestionUseCase: GetQuestionUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuizViewModel(getQuestionUseCase) as T
    }
}