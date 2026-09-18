package ua.cn.stu.tictactoe.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import ua.cn.stu.tictactoe.R

@Composable
fun LoginScreen(onLogin: (String) -> Unit) {
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var attempted by rememberSaveable { mutableStateOf(false) }

    val focus = LocalFocusManager.current

    fun submit() {
        attempted = true
        if (login.isNotBlank() && password.isNotBlank()) {
            focus.clearFocus()
            onLogin(login.trim())
        }
    }
    Box(
        Modifier.fillMaxSize().safeDrawingPadding().imePadding()
            .verticalScroll(rememberScrollState()).padding(dimensionResource(R.dimen.screen_padding)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.widthIn(max = dimensionResource(R.dimen.content_width)).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing))
        ) {
            Text(stringResource(R.string.login_title), style = MaterialTheme.typography.headlineSmall)
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.login)) },
                singleLine = true,
                isError = attempted && login.isBlank(),
                supportingText = if (attempted && login.isBlank()) {
                    { Text(stringResource(R.string.required_field)) }
                } else null,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.password)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = attempted && password.isBlank(),
                supportingText = if (attempted && password.isBlank()) {
                    { Text(stringResource(R.string.required_field)) }
                } else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { submit() })
            )
            Button(onClick = { submit() }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.sign_in))
            }
        }
    }
}
