package com.ken.aimockinterview.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.ken.aimockinterview.MainActivity
import com.ken.aimockinterview.components.AddInterviewDialog
import com.ken.aimockinterview.components.LogoutConfirmationDialog
import com.ken.aimockinterview.components.TextExtractor
import com.ken.aimockinterview.components.TopHeader
import com.ken.aimockinterview.models.InterviewDetails
import com.ken.aimockinterview.navigation.Routes
import com.ken.aimockinterview.ui.theme.darkBlue
import com.ken.aimockinterview.utils.Constants
import com.ken.aimockinterview.utils.OnBoardingUtils
import com.ken.aimockinterview.utils.Utils
import com.ken.aimockinterview.viewmodels.HomeViewModel
import com.ken.aimockinterview.viewmodels.LoginViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    context: MainActivity,
) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val state = loginViewModel.loginState.collectAsState(initial = null)

    val interviewList = homeViewModel.interviewList.collectAsState()

    val onBoardingUtils = remember {
        OnBoardingUtils(context = context)
    }

    val showDialog = rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var logoutShowDialog by remember {
        mutableStateOf(false)
    }

    var showTextExtractor by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        Log.d(Constants.TAG, "USER ID IN HOME: ${homeViewModel.userData.value.userId}")
        Log.d(Constants.TAG, "USER DATA IN HOME: ${homeViewModel.interviewList.value}")
        homeViewModel.getMockInterview()
    }

    Scaffold(
        topBar = {
            TopHeader(
                isHomeScreen = true,
                onClick = {
                    logoutShowDialog = true
                },
                onProfile = {
                    showTextExtractor = true
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            if (showTextExtractor) {
                TextExtractor()
            } else {

                PreviousInterviewList(interviewList = interviewList.value, onClick = {
                    showDialog.value = true
                }, navController = navController)
            }

            if (showDialog.value) {
                AddInterviewDialog(
                    onDismiss = {
                        showDialog.value = false
                    },
                    onNavigate = { jobRole, jobDescription, yearsOfExperience ->
                        showDialog.value = false
                        navigateTOScreen(
                            navController = navController,
                            jobRole = jobRole,
                            jobDescription = jobDescription,
                            experience = yearsOfExperience,
                            questionsList = ""
                        )
                    }
                )
            }
        }
    }

    if (logoutShowDialog) {
        LogoutConfirmationDialog(
            onDismiss = {
                logoutShowDialog = false
            },
            onConfirm = {
                loginViewModel.logoutUser()
                logoutShowDialog = false
            }
        )
    }

    LaunchedEffect(key1 = state.value?.isSuccess) {
        scope.launch {
            if (state.value?.isSuccess?.isNotEmpty() == true) {
                val success = state.value?.isSuccess
                Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                onBoardingUtils.clearUserLoggedIn()
                navController.popBackStack()
                navController.navigate(Routes.Login)
            }
        }
    }
}

fun navigateTOScreen(
    navController: NavController,
    jobRole: String,
    jobDescription: String,
    experience: String,
    questionsList: String,
    mockId: String = "",
) {
    navController.navigate(
        Routes.Interview
            (
            jobRole = jobRole,
            jobDescription = jobDescription,
            experience = experience,
            questionsList = questionsList,
            mockId = mockId
        )
    )
}

@Composable
fun DashboardHeader(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 20.dp)
    ) {
        Text(
            "Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            color = darkBlue,
            fontWeight = FontWeight.Bold,
        )

        Text(
            "Create and Start Your AI Mock Interview",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black.copy(0.5f),
            fontWeight = FontWeight.SemiBold,
            modifier = modifier.padding(top = 3.dp)
        )
    }
}

@Composable
fun CardAddNew(modifier: Modifier = Modifier, onClick: () -> Unit) {

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(0.4f)),
        border = CardDefaults.outlinedCardBorder(enabled = true), // Optional, adds border
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(horizontal = 20.dp)
            .clickable {
                onClick()
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize(),
        ) {
            Text(
                text = "+ Add New",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@Composable
fun PreviousText(modifier: Modifier = Modifier) {

    Text(
        "Previous Mock Interview",
        style = MaterialTheme.typography.headlineSmall,
        fontSize = 22.sp,
        color = Color.Black,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(start = 20.dp)
    )
}

@Composable
fun PreviousInterviewList(
    interviewList: List<InterviewDetails>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    navController: NavController,
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = modifier.padding(bottom = 20.dp)
    ) {
        item {
            Column(
                modifier = modifier.fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                DashboardHeader()
                Spacer(modifier.height(25.dp))
                CardAddNew(onClick = onClick)
                Spacer(modifier.height(30.dp))
                if (interviewList.isNotEmpty())
                    PreviousText()
            }
        }
        if (interviewList.isNotEmpty())
            items(interviewList) { item ->
                PreviousItem(item, navController = navController)
            }
        item {
            Spacer(modifier.height(20.dp))
        }
    }
}

@Composable
fun PreviousItem(
    item: InterviewDetails,
    modifier: Modifier = Modifier,
    navController: NavController,
) {

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, color = Color.LightGray.copy(0.5f)),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 120.dp)
            .padding(horizontal = 20.dp)
            .clickable {
//                onClick()
            }
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(15.dp),
        ) {
            Text(
                text = item.jobRole,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 22.sp,
                color = darkBlue,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = item.jobDescription,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp,
                color = Color.Black.copy(0.75f),
                fontWeight = FontWeight.Medium,
                modifier = modifier.padding(top = 5.dp)
            )
            Text(
                "Created At: ${Utils.convertTimestampToDate(item.createAt)}",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                color = Color.Black.copy(0.6f),
                fontWeight = FontWeight.Normal,
                modifier = modifier.padding(top = 5.dp)
            )

            Row(
                modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                OutlinedButton(onClick = {
                    navController.navigate(Routes.Feedback(item.mockId))

                }, modifier.weight(1f)) {
                    Text(
                        "Feedback",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 16.sp,
                        color = darkBlue,
                        fontWeight = FontWeight.Medium,
                        modifier = modifier.padding(4.dp)
                    )
                }
                OutlinedButton(
                    onClick = {
                        navigateTOScreen(
                            navController = navController,
                            jobRole = item.jobRole,
                            jobDescription = item.jobDescription,
                            experience = item.jobExperience,
                            questionsList = item.jsonMockResp,
                            mockId = item.mockId
                        )
                    },
                    modifier
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = darkBlue.copy(0.8f))
                ) {
                    Text(
                        "Start",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        modifier = modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}