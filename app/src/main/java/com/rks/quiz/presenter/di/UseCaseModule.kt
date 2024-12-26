package com.rks.quiz.presenter.di

import com.rks.quiz.data.repository.dataSource.QuestionRemoteDataSource
import com.rks.quiz.domain.GetQuestionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun providesQuestionUseCase(dataSourceImpl: QuestionRemoteDataSource): GetQuestionUseCase {
        return GetQuestionUseCase(dataSourceImpl)
    }
}