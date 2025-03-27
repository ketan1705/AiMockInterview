package com.ken.aimockinterview.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.ken.aimockinterview.ui.theme.darkBlue
import com.ken.aimockinterview.ui.theme.lightBlue
import com.ken.aimockinterview.ui.theme.mediumBlue
import com.ken.aimockinterview.utils.Constants
import com.ken.aimockinterview.viewmodels.GeminiViewModel

@Composable
fun AddInterviewDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onNavigate: (String, String, String) -> Unit,
) {
    var jobRole by rememberSaveable { mutableStateOf("") }
    var jobDescription by rememberSaveable { mutableStateOf("") }
    var yearsOfExperience by rememberSaveable { mutableStateOf("") }
    var isJobRoleEmpty by rememberSaveable { mutableStateOf(false) }
    var isJobDescriptionEmpty by rememberSaveable { mutableStateOf(false) }
    var isYearsOfExperienceEmpty by rememberSaveable { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val geminiViewModel: GeminiViewModel = hiltViewModel()
    val state = geminiViewModel.resumeData.collectAsState()

    Dialog(
        onDismissRequest = { false },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {

        Column {
            Icon(
                Icons.Filled.Close, contentDescription = "Close", tint = Color.White,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 20.dp)
                    .clickable {
                        onDismiss()
                    },
            )
            Surface(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp, vertical = 10.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .background(
                            color = darkBlue,
                        )
                        .border(
                            2.dp, lightBlue, RoundedCornerShape(15.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        "Tell us more about your job interview",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White, fontSize = 18.sp,
                        modifier = modifier
                            .wrapContentSize()
                            .padding(top = 20.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Add details about your job position/role, Job description and years of experience",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White.copy(0.70f), fontSize = 16.sp,
                        modifier = modifier
                            .wrapContentSize()
                            .padding(top = 10.dp),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(20.dp))
                    TextExtractor()
                    /* CustomizedText("Please Uplaod Your Resume", Color.White)

                     OutlinedButton(
                         onClick = {
                             // Launch intent to pick PDF file
                             val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                                 type = "application/pdf"
                             }
                             ctx.startActivity(intent)
                         },
                         modifier = Modifier
                             .fillMaxWidth()
                             .padding(top = 10.dp),
                         shape = RoundedCornerShape(8.dp),
                         border = BorderStroke(1.dp, Color.White)
                     ) {
                         Text(
                             "Upload PDF Resume",
                             color = Color.White,
                             modifier = Modifier.padding(8.dp)
                         )
                     }*/

                    CustomizedText("Job Role/Job Position", Color.White)

                    CustomizedTextField(
                        text = jobRole,
                        onValueChange = { jobRole = it },
                        singleLine = true,
                        minLines = 1,
                        placeHolderText = "Ex. Android Developer",
                        supportText = if (isJobRoleEmpty) "Please add job role" else "",
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    )

                    CustomizedText("Job Description/Tech Stack (In Short)", Color.White)

                    CustomizedTextField(
                        text = jobDescription,
                        onValueChange = {
                            jobDescription = it
//                            "Java, Spring Boot, Mongo DB"
                        },
                        singleLine = false,
                        minLines = 5,
                        placeHolderText = "Ex. Java, Kotlin, Jetpack Compose",
                        supportText = if (isJobDescriptionEmpty) "Please add job description" else "",
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    )

                    CustomizedText("Years Of Experience", Color.White)
                    CustomizedTextField(
                        text = yearsOfExperience,
                        onValueChange = {
                            if (it.isEmpty() || (it.all { char -> char.isDigit() } && it.length <= 2)) {
                                yearsOfExperience = it
                            }
                        },
                        singleLine = true,
                        minLines = 1,
                        placeHolderText = "Ex. 5",
                        supportText = if (isYearsOfExperienceEmpty) "Please add years of experience" else "",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
//                        keyboardActions = KeyboardActions(onDone = {
//                            Log.d(Constants.TAG, "KeyboardController: $keyboardController")
//                            keyboardController?.hide()
//                        })
                    )
                    Spacer(Modifier.height(20.dp))

                    OutlinedButton(
                        onClick = {
/*                            onNavigate(
                                jobRole.trim().toString(),
                                jobDescription.trim().toString(),
                                yearsOfExperience.trim().toString()
                            )*/
                            isJobRoleEmpty = jobRole.isEmpty()
                            isJobDescriptionEmpty = jobDescription.isEmpty()
                            isYearsOfExperienceEmpty = yearsOfExperience.isEmpty()
                            if (!isJobRoleEmpty && !isJobDescriptionEmpty && !isYearsOfExperienceEmpty) {
                                isLoading = true
                                Log.d(Constants.TAG, "Job Role: $jobRole")
                                Log.d(Constants.TAG, "Job Description: $jobDescription")
                                Log.d(Constants.TAG, "Experience: $yearsOfExperience")
                                onNavigate(
                                    jobRole.trim().toString(),
                                    jobDescription.trim().toString(),
                                    yearsOfExperience.trim().toString()
                                )
                                isLoading = false

                            }
                        },
                        modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            disabledContainerColor = Color.White
                        ),
                        enabled = !isLoading
                    ) {
                        if (!isLoading)
                            Text(
                                "Start Interview",
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 16.sp,
                                color = mediumBlue,
                                fontWeight = FontWeight.Medium,
                                modifier = modifier.padding(vertical = 8.dp)
                            ) else {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(vertical = 8.dp),
                                color = mediumBlue,
                                strokeWidth = 3.dp
                            )
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Button(
                        onClick = {
                            onDismiss()
                        }, modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            "Cancel",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            modifier = modifier.padding(vertical = 8.dp)

                        )
                    }

                }
            }
        }

        LaunchedEffect(state.value) {
            jobRole = state.value?.jobRole ?: ""
            jobDescription =
                state.value?.technicalSkills?.takeIf { it.isNotEmpty() }?.joinToString(", ") ?: ""
            yearsOfExperience = state.value?.yearsOfExperience ?: ""
        }

    }
}
