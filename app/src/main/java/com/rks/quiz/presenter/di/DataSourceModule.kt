package com.rks.quiz.presenter.di

import com.rks.quiz.data.QuestionService
import com.rks.quiz.data.repository.dataSource.QuestionRemoteDataSource
import com.rks.quiz.data.repository.dataSourceImpl.QuestionRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @Singleton
    fun providesRemoteQuestionDataSource(service: QuestionService): QuestionRemoteDataSource{
        return QuestionRemoteDataSourceImpl(service);
    }

}