package com.ken.aimockinterview.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
fun SecondLoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val state = viewModel.loginState.collectAsState(initial = null)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val utils by lazy { OnBoardingUtils(context) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordVisibility by remember { mutableStateOf(false) }

    val fadeInAlpha by animateFloatAsState(targetValue = 1f, animationSpec = tween(1000))

    val buttonScale by animateFloatAsState(
        targetValue = if (state.value?.isLoading == true) 0.95f else 1f,
        animationSpec = tween(durationMillis = 300)
    )
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
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .graphicsLayer(alpha = fadeInAlpha)
                .padding(24.dp)
                .clip(RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = "Login To Your Account",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(35.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("E-Mail") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = emailError != null,
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            Icons.Rounded.Email,
                            contentDescription = null,
                            tint = Color(0xFF1565C0)
                        )
                    },
                    supportingText = {
                        emailError?.let {
                            Text(it, color = Color.Red, fontSize = 12.sp)
                        }
                    },
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1565C0),
                        unfocusedBorderColor = Color.Gray,
                        errorBorderColor = Color.Red
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = passwordError != null,
                    leadingIcon = {
                        Icon(
                            Icons.Rounded.Lock,
                            contentDescription = null,
                            tint = Color(0xFF1565C0)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(
                                if (passwordVisibility) Icons.Default.Visibility
                                else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle Password"
                            )
                        }
                    },
                    supportingText = {
                        passwordError?.let {
                            Text(it, color = Color.Red, fontSize = 12.sp)
                        }
                    },
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1565C0),
                        unfocusedBorderColor = Color.Gray,
                        errorBorderColor = Color.Red
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        scope.launch {
                            keyboardController?.hide()
                            val (isValid, errors) = validateInputs(email, password)
                            emailError = errors.first
                            passwordError = errors.second
                            if (isValid) {
                                viewModel.loginUser(email, password)
                            }
                        }
                    },
                    modifier = Modifier
                        .scale(buttonScale)
                        .fillMaxWidth(0.6f)
                        .height(50.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text(
                        "Login",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                AnimatedVisibility(visible = state.value?.isLoading == true) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(color = Color(0xFF1565C0))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Don't have an account? Register",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Routes.Register)
                        }
                        .padding(4.dp),
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF0D47A1),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Navigation side-effects
        LaunchedEffect(state.value?.isSuccess) {
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

        LaunchedEffect(state.value?.isError) {
            scope.launch {
                if (state.value?.isError?.isNotEmpty() == true) {
                    val error = state.value?.isError
                    Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
