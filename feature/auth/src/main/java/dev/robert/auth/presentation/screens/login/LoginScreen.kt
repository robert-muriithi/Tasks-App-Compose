package dev.robert.auth.presentation.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stevdzasan.onetap.GoogleUser
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import com.stevdzasan.onetap.getUserFromTokenId
import com.stevdzasan.onetap.rememberOneTapSignInState
import dev.robert.auth.R
import dev.robert.design_system.presentation.components.SignInWithGoogleButton
import dev.robert.design_system.presentation.components.TDButton
import dev.robert.design_system.presentation.components.TDFilledTextField
import dev.robert.design_system.presentation.components.TDSpacer

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToForgotPassword: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val authState by viewModel.authenticated.collectAsStateWithLifecycle()
    val oneTapSignInState = rememberOneTapSignInState()
    var user: GoogleUser? by remember {
        mutableStateOf(null)
    }
    val showDialog = remember {
        mutableStateOf(false)
    }

    OneTapSignInWithGoogle(
        state = oneTapSignInState,
        clientId = "180548915348-ma35li7i99ka5rog7rl5jrfl9q65da3t.apps.googleusercontent.com",
        rememberAccount = false,
        onTokenIdReceived = {
            user = getUserFromTokenId(tokenId = it)
            viewModel.setAuthenticated(authenticated = true)
        },
        onDialogDismissed = {
        }
    )

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
                onLogin = {
                    viewModel.onEvent(LoginScreenEvents.LoginEvent)
                },
                modifier = Modifier.fillMaxSize(),
                onRegister = onNavigateToRegister,
                onForgotPassword = onNavigateToForgotPassword,
                onSignInWithGoogle = {},
                oneTapSignInState = oneTapSignInState
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
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    onSignInWithGoogle: () -> Unit,
    oneTapSignInState: OneTapSignInState
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
        Text(
            text = "Forgot Password?",
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clickable {
                    onForgotPassword()
                }
        )
        TDButton(
            onClick = onLogin,
            text = "Login",
            enabled = true,
            modifier = Modifier
                .fillMaxWidth(0.9f)
        )
        TDSpacer(modifier = Modifier.height(10.dp))
        SignInWithGoogleButton(
            onClick = {
                oneTapSignInState.open()
            }
        )
        TDSpacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Don't have an account?",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        TDSpacer(modifier = Modifier.height(10.dp))
        TextButton(
            onClick = onRegister,
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Text(
                text = "Register",
                style = MaterialTheme.typography.bodyLarge
            )
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
