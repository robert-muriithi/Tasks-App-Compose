package dev.robert.auth.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.robert.auth.R
import dev.robert.design_system.presentation.components.SignInWithGoogleButton
import dev.robert.design_system.presentation.components.TDButton
import dev.robert.design_system.presentation.components.TDFilledTextField
import dev.robert.design_system.presentation.components.TDLoader
import dev.robert.design_system.presentation.components.TDOrSeparator
import dev.robert.design_system.presentation.components.TDOutlinedTextField
import dev.robert.design_system.presentation.components.TDSpacer

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = R.drawable.bg),
                    contentScale = ContentScale.FillBounds,
                    sizeToIntrinsics = true,
                    alpha = 0.2f
                )
        ) {
            LoginScreenContent(
                uiState = uiState,
                onEmailChange = { email ->
                    viewModel.onEvent(
                        LoginScreenEvents.OnEmailChanged(email)
                    )
                },
                onPasswordChange = { password ->
                    viewModel.onEvent(
                        LoginScreenEvents.OnPasswordChanged(password)
                    )
                },
                onSubmit = {
                    viewModel.onEvent(LoginScreenEvents.LoginEvent)
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    uiState: LoginState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TDFilledTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = "Email",
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.mail_24px),
                    contentDescription = "Email Icon"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(53.dp)
        )
        TDSpacer(modifier = Modifier.height(10.dp))
        TDFilledTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = "Password",
            isPassword = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(53.dp)
        )
        TDSpacer(modifier = Modifier.height(10.dp))
        TDButton(
            onClick = onSubmit,
            text = "Login",
            enabled = true,
            modifier = Modifier
                .fillMaxWidth(0.9f)
        )
        // TDOrSeparator(modifier = Modifier.fillMaxWidth(0.9f))
        TDOrSeparator()
        SignInWithGoogleButton(onClick = {})
    }
}

@Composable
fun Tabs(title: String, selected: Boolean, onTap: () -> Unit) {
    Tab(selected = selected, onClick = onTap) {
        Column(
            Modifier
                .padding(10.dp)
                .height(50.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                Modifier
                    .size(10.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(
                        color = if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.background
                    )
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryTextTabs(
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: (String, String) -> Unit,

    uiState: LoginState,
    onRegister: (String, String, String) -> Unit
) {
    var state by remember { mutableStateOf(0) }
    val titles = listOf("Login", "Register")

    TDLoader(isLoading = uiState.isLoading)
    Column {
        SecondaryTabRow(
            selectedTabIndex = state
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state == index,
                    onClick = { state = index },
                    text = {
                        Text(
                            text = title,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
        when (state) {
            0 -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome! Please fill in your details to login",
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                    TDSpacer(modifier = Modifier.height(10.dp))
                    TDOutlinedTextField(
                        isPassword = false,
                        value = uiState.email,
                        label = "Email",
                        trailingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.mail_24px),
                                contentDescription = "Email Icon"
                            )
                        },
                        onValueChange = {
                            onEmailChange(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(53.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )
                    TDSpacer(modifier = Modifier.height(10.dp))
                    TDOutlinedTextField(
                        isPassword = true,
                        value = uiState.password,
                        label = "Password",
                        onValueChange = {
                            onPasswordChange(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(53.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        )
                    )
                    TDSpacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp), horizontalArrangement = Arrangement.End
                    ) {
                        Text("Forgot Password?", textAlign = TextAlign.End)
                    }
                    TDSpacer(modifier = Modifier.height(10.dp))
                    TDButton(
                        onClick = {
                            onLogin(uiState.email, uiState.password)
                        },
                        text = "Login",
                        enabled = true,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.inversePrimary,
                                        MaterialTheme.colorScheme.primary
                                    ),
                                    start = Offset(0f, 10f),
                                    end = Offset(10f, 200f)
                                )
                            )
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 20.dp,
                            vertical = 5.dp
                        )
                    )
                    SignInWithGoogleButton(onClick = {})
                }
            }
            // Register
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome! Please fill in your details to Register",
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                    TDSpacer(modifier = Modifier.height(10.dp))
                    TDSpacer(modifier = Modifier.height(10.dp))
                    TDOutlinedTextField(
                        isPassword = false,
                        value = uiState.email,
                        label = "Email",
                        trailingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.mail_24px),
                                contentDescription = "Email Icon"
                            )
                        },
                        onValueChange = onEmailChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(53.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )
                    TDSpacer(modifier = Modifier.height(10.dp))
                    TDOutlinedTextField(
                        isPassword = true,
                        value = uiState.password,
                        label = "Password",
                        onValueChange = onPasswordChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(53.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        )
                    )
                    TDSpacer(modifier = Modifier.height(10.dp))
                    TDButton(
                        onClick = {
                            onLogin(uiState.email, uiState.password)
                        },
                        text = "Register",
                        enabled = true,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.inversePrimary,
                                        MaterialTheme.colorScheme.primary
                                    ),
                                    start = Offset(0f, 10f),
                                    end = Offset(10f, 200f)
                                )
                            )
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 20.dp,
                            vertical = 5.dp
                        )
                    )
                    SignInWithGoogleButton(onClick = {})
                }
            }
        }
    }
}

@Preview(
    name = "Login",
    device = "spec:shape=Normal,width=360,height=840,unit=dp,dpi=480",
    showBackground = true
)
@Composable
fun LoginScreenPreview(modifier: Modifier = Modifier) {
    LoginScreen()
}
