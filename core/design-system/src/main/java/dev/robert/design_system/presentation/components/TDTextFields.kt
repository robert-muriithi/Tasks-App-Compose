package dev.robert.design_system.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.robert.design_system.R

@Composable
fun TDOutlinedTextField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    value: String,
    isError: Boolean = false,
    label: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    maxLines: Int = 1,
    isPassword: Boolean = false,
    content: @Composable () -> Unit = {
        OutlineTextInput(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            label = label,
            keyboardOptions = keyboardOptions,
            trailingIcon = trailingIcon,
            maxLines = maxLines,
            isPassword = isPassword,
            leadingIcon = leadingIcon
        )
    }
) {
    content()
}

@Composable
fun TDFilledTextField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    value: String,
    isError: Boolean = false,
    label: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    maxLines: Int = 1,
    isPassword: Boolean = false,
    content: @Composable () -> Unit = {
        FilledTextFilled(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            trailingIcon = trailingIcon,
            leadingIcon = leadingIcon,
            keyboardOptions = keyboardOptions,
            maxLine = maxLines,
            label = label,
            isError = isError,
            isPassword = isPassword
        )
    }
) {
    content()
}

@Composable
fun FilledTextFilled(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions,
    maxLine: Int,
    label: String?,
    isError: Boolean,
    isPassword: Boolean
) {
    var togglePassword by rememberSaveable {
        mutableStateOf(true)
    }
    TextField(
        modifier = modifier,
        value = value,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        onValueChange = onValueChange,
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        trailingIcon = {
            if (isPassword) {
                val icon = if (togglePassword) painterResource(R.drawable.visibility_on)
                else painterResource(R.drawable.visibility_off)
                IconButton(
                    onClick = { togglePassword = togglePassword.not() }
                ) {
                    Icon(
                        painter = icon,
                        contentDescription = "View Password"
                    )
                }
            } else trailingIcon?.invoke()
        },
        keyboardOptions = keyboardOptions,
        maxLines = maxLine,
        placeholder = {
            label?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        },
        leadingIcon = leadingIcon,
        visualTransformation = if (isPassword)
            if (togglePassword) PasswordVisualTransformation()
            else VisualTransformation.None
        else VisualTransformation.None,
    )
}

@Composable
fun OutlineTextInput(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    label: String?,
    keyboardOptions: KeyboardOptions,
    trailingIcon: @Composable (() -> Unit)?,
    leadingIcon: @Composable (() -> Unit)?,
    maxLines: Int,
    isPassword: Boolean
) {
    var togglePassword by rememberSaveable {
        mutableStateOf(true)
    }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        shape = RoundedCornerShape(5.dp),
        placeholder = {
            label?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        },
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        trailingIcon = {
            if (isPassword) {
                val icon = if (togglePassword) painterResource(R.drawable.visibility_on)
                else painterResource(R.drawable.visibility_off)
                IconButton(
                    onClick = { togglePassword = togglePassword.not() }
                ) {
                    Icon(
                        painter = icon,
                        contentDescription = "View Password"
                    )
                }
            } else trailingIcon?.invoke()
        },
        maxLines = maxLines,
        visualTransformation = if (isPassword)
            if (togglePassword) PasswordVisualTransformation()
            else VisualTransformation.None
        else VisualTransformation.None,
        leadingIcon = leadingIcon
    )
}

@Preview(
    name = "TextFields",
    device = "spec:shape=Normal,width=360,height=840,unit=dp,dpi=480",
    showBackground = true
)
@Composable
fun a(modifier: Modifier = Modifier) {
    TDOutlinedTextField(onValueChange = {}, value = "fjkf", label = "jhbfjh", isPassword = false, trailingIcon = { Icon(
        painter = painterResource(R.drawable.visibility_on),
        contentDescription = ""
    ) })
}
@Preview(
    name = "vjgj",
    device = "spec:shape=Normal,width=360,height=840,unit=dp,dpi=480",
    showBackground = true
)
@Composable
fun b(modifier: Modifier = Modifier) {
    TDFilledTextField(onValueChange = {}, value = "fjkf", label = "jhbfjh", isPassword = false, trailingIcon = { Icon(
        painter = painterResource(R.drawable.visibility_on),
        contentDescription = ""
    ) })
}
