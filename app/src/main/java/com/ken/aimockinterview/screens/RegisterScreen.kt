package com.ken.aimockinterview.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ken.aimockinterview.components.CustomOutlinedTextField
import com.ken.aimockinterview.navigation.Routes
import com.ken.aimockinterview.viewmodels.RegisterViewModel
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val viewModel: RegisterViewModel = hiltViewModel()
    val state = viewModel.registerState.collectAsState(initial = null)
    val context = LocalContext.current
    var name: String by remember { mutableStateOf("") }
    var email: String by remember { mutableStateOf("") }
    var password: String by remember { mutableStateOf("") }
    var nameError: String? by remember { mutableStateOf(null) }
    var emailError: String? by remember { mutableStateOf(null) }  // Error message for email
    var passwordError: String? by remember { mutableStateOf(null) }  // Error message for password
    var passWordVisibility by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    fun validateInputs(
        name: String,
        email: String,
        password: String,
    ): Pair<Boolean, Triple<String?, String?, String?>> {
        var isValid = true
        var nameError: String? = null
        var emailError: String? = null
        var passwordError: String? = null

        //validate name
        if (name.isEmpty()) {
            nameError = "Name cannot be empty"
            isValid = false
        } else if (name.length < 3) {
            nameError = "Name must be at least 3 characters"
            isValid = false
        }

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

        return Pair(isValid, Triple(nameError, emailError, passwordError))
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
            elevation = CardDefaults.cardElevation(10.dp)
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
                    text = "Create Your Account",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    style = MaterialTheme.typography.headlineMedium
                )

                /*Spacer(modifier = Modifier.height(5.dp))

                Image(
                    painter = painterResource(R.drawable.img),
                    contentDescription = null,
                    modifier = Modifier
                        .height(150.dp)
                        .width(150.dp)
                )

                Spacer(modifier = Modifier.height(5.dp))
*/
                Spacer(
                    modifier = Modifier.height(40.dp)
                )
                /*   OutlinedTextField(
                       value = name, onValueChange = { name = it },
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(horizontal = 10.dp),
                       placeholder = { Text(text = "Name") },
                       singleLine = true,
                       shape = CircleShape,
                       isError = nameError != null,
                       supportingText = {
                           nameError?.let {
                               Text(
                                   text = it,
                                   color = Color.Red,
                                   fontSize = 16.sp,
                                   fontWeight = FontWeight.Bold
                               )
                           }
                       }, leadingIcon = {
                           Icon(
                               Icons.Rounded.Person,
                               contentDescription = null,
                               tint = Color.Gray
                           )
                       },
                       keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                   )*/
                CustomOutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Name",
                    errorText = nameError,
                    leadingIcon = Icons.Rounded.Person,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                Spacer(modifier = Modifier.height(5.dp))

                CustomOutlinedTextField(
                    value = email,
                    onValueChange = { email = it.trim() },
                    placeholder = "E-Mail",
                    errorText = emailError,
                    leadingIcon = Icons.Rounded.Email,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                /*    OutlinedTextField(
                        value = email, onValueChange = { email = it.trim() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        placeholder = { Text(text = "E-Mail") },
                        singleLine = true,
                        shape = CircleShape,
                        isError = emailError != null,
                        supportingText = {
                            emailError?.let {
                                Text(
                                    text = it,
                                    color = Color.Red,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }, leadingIcon = {
                            Icon(
                                Icons.Rounded.Email,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)

                    )*/

                Spacer(modifier = Modifier.height(5.dp))
                CustomOutlinedTextField(
                    value = password,
                    onValueChange = { password = it.trim() },
                    placeholder = "Password",
                    errorText = passwordError,
                    leadingIcon = Icons.Rounded.Lock,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    isPasswordField = true
                )
                /*       OutlinedTextField(
                           value = password,
                           onValueChange = { password = it.trim() },
                           singleLine = true,
                           modifier = Modifier
                               .fillMaxWidth()
                               .padding(horizontal = 10.dp),
                           placeholder = { Text(text = "Password") },
                           shape = CircleShape,
                           isError = passwordError != null,
                           supportingText = {
                               passwordError?.let {
                                   Text(
                                       text = it,
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
                       )*/

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = {
                        scope.launch {
                            keyboardController?.hide()
                            val validationResult = validateInputs(name, email, password)
                            val isValid = validationResult.first
                            val errors = validationResult.second

                            nameError = errors.third
                            emailError = errors.first
                            passwordError = errors.second
                            if (isValid) {
                                viewModel.registerUser(name.trim(), email, password)
                            }
                        }
                    }, modifier = modifier.fillMaxWidth(0.8f)
                )
                {
                    Text(
                        text = "Create Account", fontSize = 18.sp,
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

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "I already have an account? Login", fontSize = 16.sp,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = modifier
                        .padding(vertical = 5.dp)
                        .clickable {
                            navController.navigate(Routes.Login)
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
                    navController.navigate(Routes.Login)
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