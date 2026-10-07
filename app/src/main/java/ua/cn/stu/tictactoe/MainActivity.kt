package ua.cn.stu.tictactoe

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import ua.cn.stu.tictactoe.ui.splash.SplashFragment

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useLightSystemBars()
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container, SplashFragment())
            }
        }
    }
}

internal fun ComponentActivity.useLightSystemBars() {
    enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
        navigationBarStyle = SystemBarStyle.light(Color.WHITE, Color.WHITE)
    )
}

@Composable
internal fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = colorResource(R.color.primary),
            onPrimary = colorResource(R.color.on_primary),
            background = colorResource(R.color.background),
            onBackground = colorResource(R.color.text),
            surface = colorResource(R.color.background),
            onSurface = colorResource(R.color.text),
            surfaceContainerHigh = colorResource(R.color.background),
            outline = colorResource(R.color.outline),
            error = colorResource(R.color.error),
        ),
    ) {
        Surface(modifier = Modifier.fillMaxSize(), content = content)
    }
}