package com.ken.aimockinterview.utils

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ken.aimockinterview.components.CustomOutlinedTextField
import com.ken.aimockinterview.navigation.Routes
import com.ken.aimockinterview.viewmodels.LoginViewModel
import com.ken.aimockinterview.viewmodels.RegisterViewModel
import kotlinx.coroutines.launch


// Unified AuthScreen Composable
@Composable
fun AuthScreenTEMP(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    // Local state to toggle between LOGIN and REGISTER modes
    var authMode by remember { mutableStateOf(AuthMode.LOGIN) }

    val loginViewModel: LoginViewModel? = if (authMode == AuthMode.LOGIN) hiltViewModel() else null
    val registerViewModel: RegisterViewModel? =
        if (authMode == AuthMode.REGISTER) hiltViewModel() else null

    val state = when (authMode) {
        AuthMode.LOGIN -> loginViewModel?.loginState?.collectAsState(initial = null)
        AuthMode.REGISTER -> registerViewModel?.registerState?.collectAsState(initial = null)
    } ?: return

    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordVisibility by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val fadeInAlpha by animateFloatAsState(targetValue = 1f, animationSpec = tween(1000))
    val buttonScale by animateFloatAsState(
        targetValue = if (state.value?.isLoading == true) 0.95f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    fun validateInputs(): Boolean {
        var isValid = true
        nameError = null
        emailError = null
        passwordError = null

        if (authMode == AuthMode.REGISTER && name.isEmpty()) {
            nameError = "Name cannot be empty"
            isValid = false
        } else if (authMode == AuthMode.REGISTER && name.length < 3) {
            nameError = "Name must be at least 3 characters"
            isValid = false
        }

        if (email.isEmpty()) {
            emailError = "Email cannot be empty"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Please enter a valid email address"
            isValid = false
        }

        if (password.isEmpty()) {
            passwordError = "Password cannot be empty"
            isValid = false
        } else if (password.length < 8) {
            passwordError = "Password must be at least 8 characters"
            isValid = false
        }
        return isValid
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = modifier
                .graphicsLayer(alpha = fadeInAlpha)
                .padding(24.dp)
                .clip(RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Crossfade(targetState = authMode, animationSpec = tween(500)) { mode ->
                    Text(
                        text = if (mode == AuthMode.LOGIN) "Login To Your Account" else "Create Your Account",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontSize = 28.sp,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                Spacer(modifier = Modifier.height(35.dp))

                AnimatedVisibility(
                    visible = authMode == AuthMode.REGISTER,
                    enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(
                        animationSpec = tween(
                            500
                        )
                    ),
                    exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(
                        animationSpec = tween(
                            500
                        )
                    )
                ) {
                    Column {
                        CustomOutlinedTextField(
                            value = name,
                            onValueChange = { name = it.trim() },
                            placeholder = "Name",
                            errorText = nameError,
                            leadingIcon = Icons.Rounded.Person,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                CustomOutlinedTextField(
                    value = email,
                    onValueChange = { email = it.trim() },
                    placeholder = "E-Mail",
                    errorText = emailError,
                    leadingIcon = Icons.Rounded.Email,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(12.dp))

                CustomOutlinedTextField(
                    value = password,
                    onValueChange = { password = it.trim() },
                    placeholder = "Password",
                    errorText = passwordError,
                    leadingIcon = Icons.Rounded.Lock,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    isPasswordField = true,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        scope.launch {
                            keyboardController?.hide()
                            if (validateInputs()) {
                                if (authMode == AuthMode.LOGIN) {
                                    loginViewModel?.loginUser(email, password)
                                } else {
                                    registerViewModel?.registerUser(name, email, password)
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .scale(buttonScale)
                        .fillMaxWidth(0.6f)
                        .height(50.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                ) {
                    Crossfade(targetState = authMode, animationSpec = tween(500)) { mode ->
                        Text(
                            text = if (mode == AuthMode.LOGIN) "Login" else "Create Account",
                            fontSize = 18.sp,
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                AnimatedVisibility(visible = state.value?.isLoading == true) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(color = Color(0xFF1565C0))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Crossfade(targetState = authMode, animationSpec = tween(500)) { mode ->
                    Text(
                        text = if (mode == AuthMode.LOGIN) "Don't have an account? Register" else "Already have an account? Login",
                        modifier = Modifier
                            .clickable {
                                // Toggle authMode instead of navigating
                                authMode =
                                    if (mode == AuthMode.LOGIN) AuthMode.REGISTER else AuthMode.LOGIN
                            }
                            .padding(4.dp),
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF1565C0),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        LaunchedEffect(state.value?.isSuccess) {
            scope.launch {
                state.value?.isSuccess?.takeIf { it.isNotEmpty() }?.let { success ->
                    Toast.makeText(context, success, Toast.LENGTH_LONG).show()
                    if (authMode == AuthMode.LOGIN) {
                        navController.popBackStack()
                        navController.navigate(Routes.Home)
                    } else {
                        // After successful registration, switch to login mode
                        authMode = AuthMode.LOGIN
                    }
                }
            }
        }

        LaunchedEffect(state.value?.isError) {
            scope.launch {
                state.value?.isError?.takeIf { it.isNotEmpty() }?.let { error ->
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
/*

// Reusable CustomOutlinedTextField
@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    errorText: String?,
    leadingIcon: ImageVector,
    keyboardOptions: KeyboardOptions,
    isPasswordField: Boolean = false,
    passwordVisibility: Boolean = false,
    onPasswordVisibilityToggle: (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = placeholder) },
        singleLine = true,
        isError = errorText != null,
        supportingText = {
            errorText?.let {
                Text(it, color = Color.Red, fontSize = 12.sp)
            }
        },
        shape = RoundedCornerShape(50),
        leadingIcon = { Icon(leadingIcon, contentDescription = null, tint = Color.Gray) },
        trailingIcon = if (isPasswordField) {
            {
                IconButton(onClick = { onPasswordVisibilityToggle?.invoke() }) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Password"
                    )
                }
            }
        } else null,
        visualTransformation = if (isPasswordField && !passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF1565C0),
            unfocusedBorderColor = Color.Gray,
            errorBorderColor = Color.Red
        ),
        keyboardOptions = keyboardOptions
    )
}
*/
