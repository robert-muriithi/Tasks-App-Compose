package dev.robert.auth.presentation.screens.login

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.identity.Identity
import dev.robert.auth.R
import dev.robert.auth.domain.model.GoogleSignResult
import dev.robert.auth.presentation.utils.GoogleAuthSignInClient
import dev.robert.design_system.presentation.components.SignInWithGoogleButton
import dev.robert.design_system.presentation.components.TDButton
import dev.robert.design_system.presentation.components.TDFilledTextField
import dev.robert.design_system.presentation.components.TDSpacer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToForgotPassword: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val applicationContext = LocalContext.current.applicationContext

    val googleAuthUiClient by lazy {
        GoogleAuthSignInClient(
            webClientId = applicationContext.getString(R.string.default_web_client_id),
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                scope.launch {
                    result.data?.let { googleAuthUiClient.signInWithIntent(it) }?.run {
                        viewModel.onEvent(
                            LoginScreenEvents.OnSignInWithGoogle(
                                this
                            )
                        )
                    }
                }
            }
            else -> {
                viewModel.onEvent(
                    LoginScreenEvents.OnSignInWithGoogle(
                        GoogleSignResult(errorMsg = "Error ${result.resultCode}")
                    )
                )
            }
        }
    }
    LaunchedEffect(key1 = uiState.isAuthenticated) {
        viewModel.action.collectLatest { value: LoginAction ->
            when (value) {
                is LoginAction.NavigateToHome -> {
                    Toast.makeText(applicationContext, "Log in success", Toast.LENGTH_SHORT).show()
                    uiState.user?.let { onNavigateToHome() }
                    viewModel.onEvent(LoginScreenEvents.OnResetState)
                }
                is LoginAction.ShowError -> {
                    Toast.makeText(applicationContext, value.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(
        modifier = Modifier
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
            onSignInWithGoogle = {
                scope.launch {
                    viewModel.onEvent(
                        LoginScreenEvents.GoogleSignInEvent(
                            client = googleAuthUiClient,
                            launcher = launcher
                        )
                    )
                }
            },
        )
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
                .height(53.dp),
            isError = uiState.emailError != null,
            isLoading = uiState.isLoading
        )
        if (uiState.emailError != null) {
            Row(modifier = Modifier.fillMaxWidth(0.9f)) {
                Text(
                    text = uiState.emailError,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else TDSpacer(modifier = Modifier.height(10.dp))
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
                .height(53.dp),
            isError = uiState.passwordError != null,
            isLoading = uiState.isLoading
        )
        if (uiState.passwordError != null)
            Row(modifier = Modifier.fillMaxWidth(0.9f)) {
                Text(
                    text = uiState.passwordError,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        else TDSpacer(modifier = Modifier.height(10.dp))
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
            enabled = uiState.isLoading.not(),
            modifier = Modifier
                .fillMaxWidth(0.9f),
            isLoading = uiState.isLoading &&
                uiState.signInOption == SignInOption.EmailAndPassword
        )
        TDSpacer(modifier = Modifier.height(10.dp))
        SignInWithGoogleButton(
            onClick = {
                onSignInWithGoogle()
            },
            isLoading = uiState.isLoading &&
                uiState.signInOption == SignInOption.Google
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
