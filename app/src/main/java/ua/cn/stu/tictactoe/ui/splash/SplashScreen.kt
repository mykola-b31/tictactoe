package ua.cn.stu.tictactoe.ui.splash

import android.os.SystemClock
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.delay
import ua.cn.stu.tictactoe.R

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val startedAt = rememberSaveable { SystemClock.elapsedRealtime() }
    val currentOnFinished by rememberUpdatedState(onFinished)

    LaunchedEffect(Unit) {
        val elapsed = (SystemClock.elapsedRealtime() - startedAt).coerceAtLeast(0L)
        delay((2_000L - elapsed).coerceAtLeast(0L))
        currentOnFinished()
    }
    Box(Modifier.fillMaxSize().safeDrawingPadding(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painterResource(R.drawable.app_logo),
                contentDescription = stringResource(R.string.logo_description),
                modifier = Modifier.size(dimensionResource(R.dimen.logo_size))
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.spacing)))
            Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineSmall)
        }
    }
}