package com.rks.quiz.presenter.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.rks.quiz.data.model.DataOrException
import com.rks.quiz.data.model.Question
import com.rks.quiz.domain.GetQuestionUseCase
import kotlinx.coroutines.launch

class QuizViewModel(private val questionUseCase: GetQuestionUseCase) : ViewModel() {

    val data: MutableState<DataOrException<ArrayList<Question>,
            Boolean, Exception>> =
        mutableStateOf(DataOrException(null, true, Exception("")))

    init {
        getAllQuestions()
    }

    fun getAllQuestions(){
        viewModelScope.launch {
            data.value.loading = true
            data.value = questionUseCase.execute()

            if(data.value.data.toString().isNotEmpty()){
                data.value.loading = false
            }

        }

    }

    fun getTotalQuestionCount(): Int? = data.value.data?.toMutableList()?.size

}