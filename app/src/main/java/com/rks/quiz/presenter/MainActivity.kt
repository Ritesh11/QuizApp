package com.rks.quiz.presenter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.composableLambdaInstance
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rks.quiz.data.model.Question
import com.rks.quiz.presenter.screens.QuizViewModel
import com.rks.quiz.presenter.screens.QuizViewModelFactory
import com.rks.quiz.ui.theme.AppColors
import com.rks.quiz.ui.theme.QuizTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var factory: QuizViewModelFactory

    private lateinit var viewModel: QuizViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, factory)[QuizViewModel::class.java]

        setContent {
            QuizTheme {

                Questions(viewModel)
            }
        }
    }
}

@Composable
fun Questions(viewModel: QuizViewModel) {
    val questionIndex = remember {
        mutableStateOf(0)
    }

    if (viewModel.data.value.loading == true) {
        CircularProgressIndicator()
    } else {
        val questions = viewModel.data.value.data?.toMutableList()
        val quesItem = try {
            questions?.get(questionIndex.value)
        } catch (ex: Exception) {
            null
        }


        if (questions != null) {
            QuestionDisplay(
                questionItem = quesItem!!,
                questionIndex = questionIndex,
                viewModel = viewModel
            ) { index ->
                questionIndex.value = index + 1
            }
        }
    }
}

@Composable
fun QuestionDisplay(
    modifier: Modifier = Modifier,
    questionItem: Question,
    questionIndex: MutableState<Int>,
    viewModel: QuizViewModel,
    onNextClicked: (Int) -> Unit = {}
) {

    val choicesState = remember(questionItem) {
        questionItem.choices.toMutableList()
    }

    val answerState = remember(questionItem) {
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(questionItem) {
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswer: (Int) -> Unit = remember(questionItem) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == questionItem.answer
        }
    }
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = AppColors.DarkPurple
    ) {
        Column(
            modifier = modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            if (questionIndex.value >= 2) ShowProgress(score = questionIndex.value)
            QuestionTracker(counter = questionIndex.value, outOff = viewModel.getTotalQuestionCount())
            DrawDottedLine(pathEffect)

            Column {
                Text(
                    text = questionItem.question,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    color = AppColors.OffWhite,
                    modifier = modifier
                        .padding(6.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(.3f)
                )
            }

            choicesState.forEachIndexed { index, answerText ->

                Row(
                    modifier = modifier
                        .padding(3.dp)
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(
                            width = 4.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    AppColors.LightPurple,
                                    AppColors.LightPurple
                                )
                            ),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .clip(RoundedCornerShape(50))
                        .background(color = Color.Transparent),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    RadioButton(
                        selected = (answerState.value == index), onClick = {
                            updateAnswer(index)
                        },
                        modifier = modifier.padding(16.dp),
                        colors = RadioButtonDefaults.colors(
                            selectedColor =
                            if (correctAnswerState.value == true
                                && index == answerState.value
                            ) {
                                Color.Green.copy(alpha = 0.2f)
                            } else {
                                Color.Red.copy(alpha = 0.2f)
                            }
                        )
                    )

                    val anotatedString = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Light,
                                color = if (correctAnswerState.value == true
                                    && index == answerState.value
                                ) {
                                    Color.Green
                                } else if (correctAnswerState.value == false
                                    && index == answerState.value
                                ) {
                                    Color.Red
                                } else {
                                    AppColors.OffWhite
                                },
                                fontSize = 17.sp
                            )
                        ) {
                            append(answerText)
                        }
                    }
                    Text(
                        text = anotatedString, modifier = modifier
                            .padding(6.dp)
                    )

                }
            }

            Button(
                onClick = {
                    onNextClicked(questionIndex.value)
                }, modifier = modifier
                    .padding(3.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue // Background color when pressed
                )
            ) {
                Text(
                    text = "Next", modifier = modifier
                        .padding(6.dp),
                    color = AppColors.OffWhite,
                    fontSize = 17.sp
                )
            }

        }

    }
}


@Composable
fun QuestionTracker(
    counter: Int = 10, outOff: Int? = 100,
    modifier: Modifier = Modifier
) {

    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
            withStyle(
                style = SpanStyle(
                    color = AppColors.LightGrey,
                    fontWeight = FontWeight.Bold,
                    fontSize = 27.sp
                )
            ) {
                append("Question $counter/")
                withStyle(
                    style = SpanStyle(
                        color = AppColors.LightGrey,
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp
                    )
                ) {
                    append("$outOff")
                }
            }
        }
    }, modifier = modifier.padding(20.dp))

}


@Composable
fun DrawDottedLine(
    pathEffect: PathEffect,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = AppColors.LightGrey,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )
    }
}


@Preview
@Composable
fun ShowProgress(
    score: Int = 12,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.linearGradient(
        listOf(
            Color(0xFFF95075),
            Color(0xFFBE6BE5)
        )
    )

    val progressFactor by remember(score) {
        mutableStateOf(score * 0.005f)
    }

    Row(
        modifier = modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(35.dp)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppColors.LightPurple, AppColors.LightPurple
                    )
                ),
                shape = RoundedCornerShape(30.dp)
            )
            .clip(RoundedCornerShape(50))
            .background(color = Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {},
            contentPadding = PaddingValues(1.dp),
            modifier = modifier
                .fillMaxWidth(progressFactor)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = buttonColors(
                containerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            )
        ) {
            Text(
                text = (score * 10).toString(),
                modifier = modifier.clip(
                    shape = RoundedCornerShape(23)
                )
                    .fillMaxHeight(0.87f)
                    .fillMaxWidth()
                    .padding(6.dp),
                color = AppColors.OffWhite,
                textAlign = TextAlign.Center
            )
        }
    }
}
