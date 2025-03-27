package com.ken.aimockinterview.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ken.aimockinterview.components.CameraColumn
import com.ken.aimockinterview.components.CustomizedText
import com.ken.aimockinterview.components.InformationBox
import com.ken.aimockinterview.components.QuestionsBox
import com.ken.aimockinterview.models.QuesAnsModel
import com.ken.aimockinterview.models.UserAnswerResponse
import com.ken.aimockinterview.navigation.Routes
import com.ken.aimockinterview.states.VoiceToTextParserState
import com.ken.aimockinterview.ui.theme.darkBlue
import com.ken.aimockinterview.ui.theme.mediumBlue
import com.ken.aimockinterview.utils.Constants
import com.ken.aimockinterview.utils.Utils
import com.ken.aimockinterview.viewmodels.GeminiViewModel
import com.ken.aimockinterview.viewmodels.TTSViewModel
import com.ken.aimockinterview.viewmodels.VoiceToTextParserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterviewScreen(
    details: Routes.Interview,
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    val geminiViewModel: GeminiViewModel = hiltViewModel()
    val ttsViewModel: TTSViewModel = hiltViewModel()
    val voiceToTextParserViewModel: VoiceToTextParserViewModel = hiltViewModel()
    val state = geminiViewModel.questionList.collectAsState()
    val voiceToTextState = voiceToTextParserViewModel.state.collectAsState()
    var canRecord by rememberSaveable {
        mutableStateOf<Boolean>(false)
    }
    var currentQuestionIndex = remember { mutableIntStateOf(0) }
    var isInterviewStarted = rememberSaveable {
        mutableStateOf<Boolean>(false)
    }
    var userAnswer by remember {
        mutableStateOf("")
    }
    var questionsList by remember {
        mutableStateOf<List<QuesAnsModel>>(emptyList())
    }
    //TODO Adding this on temporary basis after that change it by using state
    var isLoadingVisible by remember {
        mutableStateOf(false)
    }


    val recordLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            canRecord = isGranted
        }
    )
    LaunchedEffect(recordLauncher) {
        recordLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
    }

    Scaffold { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .verticalScroll(
                        rememberScrollState()
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(Modifier.height(10.dp))

                Text(
                    "Let's Get Started",
                    style = MaterialTheme.typography.headlineMedium,
                    color = darkBlue,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.align(Alignment.Start)
                )
                Spacer(Modifier.height(5.dp))
                if (!isInterviewStarted.value) {
                    DefaultBox(details)
                    StartButton(
                        onClick = {
                            isLoadingVisible = true
                            if (details.questionsList.isNotEmpty()) {
                                val result = Utils.convertJsonToList(details.questionsList)
                                questionsList = result
                                geminiViewModel.saveMockId(details.mockId)
                            } else {
                                geminiViewModel.getQuestions(
                                    details.jobRole,
                                    details.jobDescription,
                                    details.experience
                                )
                            }
                        },
                        isLoading = !isLoadingVisible
                    )
//                    if (isLoadingVisible)
//                        CircularProgressIndicator()

                } else {
                    isLoadingVisible = false
                    SecondaryBox(
                        modifier, questionsList, currentQuestionIndex.intValue,
                        voiceToTextState,
                        onClick = {
                            if (voiceToTextState.value.isSpeaking) {
                                voiceToTextParserViewModel.stopListening()
                            } else {
                                voiceToTextParserViewModel.startListening()
                            }
                        }, nextButtonClick = {
                            if (currentQuestionIndex.intValue < questionsList.size - 1) {

                                geminiViewModel.getFeedback(
                                    question = questionsList[currentQuestionIndex.intValue].question,
                                    userAnswer = userAnswer,
                                    UserAnswerResponse(
                                        question = questionsList[currentQuestionIndex.intValue].question,
                                        correctAns = questionsList[currentQuestionIndex.intValue].answer,
                                        userAns = userAnswer,
                                        mockId = details.mockId,
                                        feedback = "",
                                        rating = "",
                                        userId = "",
                                        questionId = "Question_${currentQuestionIndex.intValue + 1}",
                                    )
                                )
                                currentQuestionIndex.intValue++
                            } else {
                                /*geminiViewModel.addUserResponse(
                                    UserAnswerResponse(
                                        state.value[currentQuestionIndex.intValue].question,
                                        state.value[currentQuestionIndex.intValue].answer,
                                        userAns = userAnswer,
                                        mockId = "",
                                        feedback = "",
                                        rating = "",
                                        userId = "",
                                        questionId = "Question_${currentQuestionIndex.intValue + 1}",
                                    )
                                )*/
                                geminiViewModel.getFeedback(
                                    question = questionsList[currentQuestionIndex.intValue].question,
                                    userAnswer = userAnswer,
                                    UserAnswerResponse(
                                        question = questionsList[currentQuestionIndex.intValue].question,
                                        correctAns = questionsList[currentQuestionIndex.intValue].answer,
                                        userAns = userAnswer,
                                        mockId = details.mockId,
                                        feedback = "",
                                        rating = "",
                                        userId = "",
                                        questionId = "Question_${currentQuestionIndex.intValue + 1}",
                                    )
                                )
                                navController.navigate(Routes.Feedback(details.mockId))
                            }
                            userAnswer = ""
                        },
                        userAnswer = userAnswer,  // Pass userAnswer
                        onUserAnswerChange = { userAnswer = it },
                        onSpeak = {
                            ttsViewModel.speak(questionsList[currentQuestionIndex.intValue].question)
                        }
                    )
                }

                InformationBox(modifier = modifier)
                Spacer(Modifier.height(15.dp))
            }
        }
    }

    LaunchedEffect(key1 = state.value) {
        if (state.value.isNotEmpty()) {
            questionsList = state.value
            isInterviewStarted.value = true
        }
    }

    LaunchedEffect(key1 = voiceToTextState.value.spokenText) {
        userAnswer = userAnswer + voiceToTextState.value.spokenText
        Log.d(
            Constants.TAG,
            "Spoken Text: $userAnswer"
        )
    }

    BackHandler(enabled = isInterviewStarted.value) {
        navController.popBackStack()
        navController.navigate(Routes.Home)
    }
}

@Composable
fun DefaultBox(
    details: Routes.Interview,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(15.dp),
        modifier = modifier
            .fillMaxWidth(),
        color = Color.LightGray.copy(0.4f),
        border = BorderStroke(1.dp, Color.LightGray),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            CustomizedText("Job Role/Job Position", Color.Black)
            CustomizedTextBody(details.jobRole)
            CustomizedText("Job Description/Tech Stack", Color.Black)
            CustomizedTextBody(details.jobDescription)
            CustomizedText("Years Of Experience", Color.Black)
            CustomizedTextBody(details.experience)
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun SecondaryBox(
    modifier: Modifier = Modifier,
    questionList: List<QuesAnsModel>,
    currentQuestionIndex: Int,
    voiceToTextState: State<VoiceToTextParserState>,
    onClick: () -> Unit,
    nextButtonClick: () -> Unit,
    userAnswer: String,  // Receive userAnswer
    onUserAnswerChange: (String) -> Unit,
    onSpeak: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        CameraColumn(
            modifier,
            voiceToTextState,
            onClick = onClick,
            nextButtonClick = nextButtonClick
        )
        QuestionsBox(
            questionList = questionList,
            currentQuestionIndex = currentQuestionIndex,
            onSpeak = onSpeak
        )

        Text(
            "Your Answer: ",
            style = MaterialTheme.typography.titleSmall,
            color = Color.Black, fontSize = 18.sp,
            modifier = modifier
                .wrapContentSize()
                .padding(top = 15.dp),
            fontWeight = FontWeight.Medium
        )

        TextField(
            value = userAnswer,
            onValueChange = onUserAnswerChange,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 3.dp),
            shape = RoundedCornerShape(10.dp),
            minLines = 3,
            singleLine = false,
            placeholder = {
                Text(
                    "Enter Your Answer",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White.copy(0.8f), fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            },
//            keyboardOptions = keyboardOptions,
//            keyboardActions = keyboardActions,

            colors = TextFieldDefaults.colors(
                focusedContainerColor = darkBlue.copy(0.8f),
                unfocusedContainerColor = darkBlue.copy(0.8f),
                disabledContainerColor = darkBlue.copy(0.8f),
                cursorColor = Color.White.copy(0.8f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        Spacer(Modifier.height(5.dp))
    }
}

@Composable
fun CustomizedTextBody(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text,
        style = MaterialTheme.typography.bodySmall,
        color = mediumBlue, fontSize = 16.sp,
        modifier = modifier
            .wrapContentSize()
            .padding(top = 5.dp),
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun StartButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isLoading: Boolean,
) {
    OutlinedButton(
        onClick = {
            onClick()
//                        if (currentQuestionIndex.intValue < state.value.size - 1) {
//                            currentQuestionIndex.intValue++
//                        }
        },
        modifier
            .fillMaxWidth(0.8f),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = mediumBlue,
            disabledContainerColor = mediumBlue
        ),
        border = BorderStroke(color = Color.LightGray, width = 1.dp),
        enabled = isLoading
    ) {
        if (isLoading)
            Text(
                "Start Interview",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                modifier = modifier.padding(vertical = 8.dp)
            )
        else
            CircularProgressIndicator(
                modifier = Modifier
                    .size(32.dp),
                strokeWidth = 3.dp,
                color = Color.White
            )
    }
}