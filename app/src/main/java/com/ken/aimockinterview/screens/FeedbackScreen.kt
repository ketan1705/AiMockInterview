package com.ken.aimockinterview.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.UnfoldLess
import androidx.compose.material.icons.outlined.UnfoldMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ken.aimockinterview.components.LoadingAnimationScreen
import com.ken.aimockinterview.components.TopHeader
import com.ken.aimockinterview.models.UserAnswerResponse
import com.ken.aimockinterview.navigation.Routes
import com.ken.aimockinterview.utils.Constants
import com.ken.aimockinterview.viewmodels.GeminiViewModel

@Composable
fun FeedbackScreen(mockId: String, navController: NavController, modifier: Modifier = Modifier) {

    var showFeedback by remember {
        mutableStateOf(mapOf<Int, Boolean>())
    }
    val listState = rememberLazyListState()
    val geminiViewModel: GeminiViewModel = hiltViewModel()
    val state = geminiViewModel.userFeedback.collectAsState()

    Scaffold(
        topBar = {
            TopHeader(
                isHomeScreen = false, onClick = {

                },
                onProfile = {

                })
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
        {
            if (state.value.isEmpty()) {
                Log.d(Constants.TAG, "FeedList: ${state.value}")
                LoadingAnimationScreen()
            } else {
                Log.d(Constants.TAG, "FeedList: ${state.value}")
                LazyColumn(
                    state = listState,
                    modifier = modifier
                        .fillMaxSize()
                        .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)

                ) {
                    item {
                        FeedbackHeader(ratingValue = calculateRating(response = state.value))
                    }

                    items(state.value.indices.toList()) { index ->
                        val questionData = state.value[index]
                        var isExpanded = showFeedback[index] == true
                        QuestionBox(onClick = {
                            showFeedback = showFeedback.toMutableMap().apply {
                                this[index] = this[index] != true
                            }
                        }, isExpanded = isExpanded, questionData = questionData)

                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = expandVertically(
                                expandFrom = Alignment.Top,
                                animationSpec = tween(durationMillis = 50)
                            ),
                            exit = shrinkVertically(
                                shrinkTowards = Alignment.Top,
                                animationSpec = tween(durationMillis = 50)
                            )
                        ) {
                            QuestionFeedbackBox(questionData = questionData)

                        }
//                        if (isExpanded)
//                            QuestionFeedbackBox(questionData = questionData)
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        geminiViewModel.getUserResponse(mockID = mockId)
    }

    BackHandler {
        navController.popBackStack()
        navController.navigate(Routes.Home)
    }
}

fun calculateRating(response: List<UserAnswerResponse>): Double {
    val ratings = response.mapNotNull { it.rating.toDoubleOrNull() }
    return if (ratings.isNotEmpty()) ratings.average() else 0.0
}

@Composable
fun FeedbackHeader(modifier: Modifier = Modifier, ratingValue: Double) {

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            "Congratulations!",
            fontSize = 28.sp,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0XFF229116)
        )

        Text(
            "Here is your interview feedback",
            fontSize = 24.sp,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            text = buildAnnotatedString {
                append("Your overall interview rating: ")
                withStyle(
                    style =
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                        )
                ) {
                    append("$ratingValue/10")
                }
            },
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            color = Color(0XFF3918de),
            modifier = modifier.padding(top = 18.dp)
        )
        Text(
            text = "Find below interview question with correct answer, Your answer and feedback for improvement",
            fontSize = 15.sp,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = modifier.padding(top = 10.dp)
        )

    }
}

@Composable
fun QuestionBox(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isExpanded: Boolean,
    questionData: UserAnswerResponse,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clickable(onClick = {
                onClick()
            }),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray.copy(0.4f),
        )
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
        ) {
            Text(
//                text = "Tell me about a time you had to work in a team to solve a complex problem. What was your role, and how did you contribute to the solution?",
                text = questionData.question,
                modifier = modifier
                    .weight(1f)
                    .padding(15.dp),
                lineHeight = 20.sp,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                style = MaterialTheme.typography.labelSmall
            )
            Icon(
                if (isExpanded) Icons.Outlined.UnfoldLess else Icons.Outlined.UnfoldMore,
                contentDescription = null,
                modifier = modifier
                    .padding(10.dp)
                /*  .clickable(
                      onClick = {
                          onClick()
                      }
                  )*/
            )
        }
    }
}

@Composable
fun QuestionFeedbackBox(modifier: Modifier = Modifier, questionData: UserAnswerResponse) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp, end = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CustomizedBox(
            title = "Rating: ",
            body = questionData.rating,
            backgroundColor = Color.White,
            titleColor = Color.Red.copy(0.8f)
        )
        CustomizedBox(
            title = "Your Answer: ",
//            body = "In a recent project, I encountered a bug causing unexpected data loss when the app was backgrounded. I systematically used Android Studio's debugger, adding breakpoints to trace the data flow. I identified that the 'onPause()' method wasn't correctly saving the data to SharedPreferences due to an incorrect key being used. Correcting the key resolved the issue and prevented further data loss.",
            body = questionData.userAns,
            backgroundColor = Color(0XFFf1a7a9).copy(0.3f),
            titleColor = Color(0XFF800f2f)
        )
        CustomizedBox(
            title = "Correct Answer: ",
//            body = "In a recent project, I encountered a bug causing unexpected data loss when the app was backgrounded. I systematically used Android Studio's debugger, adding breakpoints to trace the data flow. I identified that the 'onPause()' method wasn't correctly saving the data to SharedPreferences due to an incorrect key being used. Correcting the key resolved the issue and prevented further data loss.",
            body = questionData.correctAns,
            backgroundColor = Color(0XFFb7e4c7).copy(0.4f),
            titleColor = Color(0XFF132a13)
        )
        CustomizedBox(
            title = "Feedback: ",
//            body = "In a recent project, I encountered a bug causing unexpected data loss when the app was backgrounded. I systematically used Android Studio's debugger, adding breakpoints to trace the data flow. I identified that the 'onPause()' method wasn't correctly saving the data to SharedPreferences due to an incorrect key being used. Correcting the key resolved the issue and prevented further data loss.",
            body = questionData.feedback,
            backgroundColor = Color(0XFF4ea8de).copy(0.2f),
            titleColor = Color(0XFF023e7d)
        )
    }
}

@Composable
fun CustomizedBox(
    modifier: Modifier = Modifier,
    title: String,
    body: String,
    backgroundColor: Color,
    titleColor: Color,
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
            .fillMaxWidth(),
        color = backgroundColor,
        border = BorderStroke(1.dp, Color.LightGray),
    ) {
        Text(
            text = buildAnnotatedString {
                append(title)
                withStyle(
                    style =
                        SpanStyle(
                            fontWeight = FontWeight.Normal,
                        )
                ) {
                    append(body)
                }
            },
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 22.sp,
            fontSize = 16.sp,
            color = titleColor,
            fontWeight = FontWeight.ExtraBold,
            modifier = modifier.padding(12.dp)
        )
    }
}