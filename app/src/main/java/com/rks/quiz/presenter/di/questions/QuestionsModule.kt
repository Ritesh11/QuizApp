package com.rks.quiz.presenter.di.questions

import com.rks.quiz.domain.GetQuestionUseCase
import com.rks.quiz.presenter.screens.QuizViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
class QuestionsModule {

    @ActivityScoped
    @Provides
    fun providesQuestionViewModelFactory(useCae: GetQuestionUseCase): QuizViewModelFactory {
        return QuizViewModelFactory(useCae)
    }

}