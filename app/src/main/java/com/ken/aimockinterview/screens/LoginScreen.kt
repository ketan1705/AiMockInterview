package com.ken.aimockinterview.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ken.aimockinterview.navigation.Routes
import com.ken.aimockinterview.utils.Constants.TAG
import com.ken.aimockinterview.utils.OnBoardingUtils
import com.ken.aimockinterview.viewmodels.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val state = viewModel.loginState.collectAsState(initial = null)
    val context = LocalContext.current
    var email: String by remember { mutableStateOf("") }
    var password: String by remember { mutableStateOf("") }
    var emailError: String? by remember { mutableStateOf(null) }  // Error message for email
    var passwordError: String? by remember { mutableStateOf(null) }  // Error message for password
    var passWordVisibility by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val utils by lazy {
        OnBoardingUtils(context)
    }

    fun validateInputs(
        email: String,
        password: String,
    ): Pair<Boolean, Pair<String?, String?>> {
        try {
            var isValid = true
            var emailError: String? = null
            var passwordError: String? = null

            // Validate email
            if (email.isEmpty()) {
                emailError = "Email cannot be empty"
                isValid = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailError = "Please enter a valid email address"
                isValid = false
            }

            // Validate password
            if (password.isEmpty()) {
                passwordError = "Password cannot be empty"
                isValid = false
            } else if (password.length < 8) {
                passwordError = "Password must be at least 8 characters"
                isValid = false
            }

            return Pair(isValid, Pair(emailError, passwordError))
        } catch (e: Exception) {
            Log.d(TAG, "error: ${e.localizedMessage}")
            throw e
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {
        Card(
            modifier = modifier
                .wrapContentSize()
                .padding(30.dp)
                .clip(
                    RoundedCornerShape(12.dp)
                )
                .background(Color.LightGray),
            elevation = CardDefaults.cardElevation(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 32.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = "Login To Your Account",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(
                    modifier = Modifier.height(40.dp)
                )
                /*
                                Image(
                                    painter = painterResource(R.drawable.img),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(150.dp)
                                        .width(150.dp)
                                )
                                Spacer(
                                    modifier = Modifier.height(5.dp)
                                )
                */
                OutlinedTextField(
                    value = email, onValueChange = { email = it.trim() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    placeholder = { Text(text = "E-Mail", color = Color.Black) },
                    singleLine = true,
                    isError = emailError != null,
                    supportingText = {
                        emailError?.let {
                            Text(
                                it,
                                color = Color.Red,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    shape = CircleShape,
                    leadingIcon = {
                        Icon(
                            Icons.Rounded.Email,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                Spacer(
                    modifier = Modifier.height(5.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it.trim() },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    placeholder = { Text(text = "Password", color = Color.Black) },
                    shape = CircleShape,
                    isError = passwordError != null,
                    supportingText = {
                        passwordError?.let {
                            Text(
                                it,
                                color = Color.Red,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Rounded.Lock,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            passWordVisibility = !passWordVisibility
                        }) {
                            Icon(
                                if (!passWordVisibility) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                                contentDescription = "Password"
                            )
                        }
                    },
                    visualTransformation = if (!passWordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                Spacer(
                    modifier = Modifier.height(10.dp)
                )
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            keyboardController?.hide()
                            val validationResult = validateInputs(email, password)
                            val isValid = validationResult.first
                            val errors = validationResult.second

                            emailError = errors.first
                            passwordError = errors.second
                            if (isValid) {
                                viewModel.loginUser(email, password)
                            }
                        }
                    },
                    modifier = modifier.fillMaxWidth(0.6f),
                )
                {
                    Text(
                        text = "Login", fontSize = 18.sp,
                        color = Color.Black,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = modifier.padding(vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.height(if (state.value?.isLoading == true) 7.dp else 0.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                )
                {
                    if (state.value?.isLoading == true) {
                        CircularProgressIndicator()
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    "Don't have an account? Register", fontSize = 16.sp,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = modifier
                        .padding(vertical = 5.dp)
                        .clickable {
                            navController.navigate(Routes.Register)
                        },
                    textAlign = TextAlign.Center,
                    color = Color.Blue.copy(0.7f)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        LaunchedEffect(key1 = state.value?.isSuccess) {
            scope.launch {
                if (state.value?.isSuccess?.isNotEmpty() == true) {
                    val success = state.value?.isSuccess
                    Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                    utils.setUserLoggedIn()
                    navController.popBackStack()
                    navController.navigate(Routes.Home)
                }
            }
        }
        LaunchedEffect(key1 = state.value?.isError) {
            scope.launch {
                if (state.value?.isError?.isNotEmpty() == true) {
                    val error = state.value?.isError
                    Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}