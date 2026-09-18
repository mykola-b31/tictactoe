package ua.cn.stu.tictactoe

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ua.cn.stu.tictactoe.ui.login.LoginScreen

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useLightSystemBars()
        setContent {
            AppTheme {
                LoginScreen { login ->
                    startActivity(Intent(this, GameActivity::class.java).apply {
                        putExtra(GameActivity.EXTRA_LOGIN, login)
                    })
                    finish()
                }
            }
        }
    }
}
