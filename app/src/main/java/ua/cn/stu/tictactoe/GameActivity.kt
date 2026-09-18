package ua.cn.stu.tictactoe

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import ua.cn.stu.tictactoe.ui.game.GameScreen

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val login = intent.getStringExtra(EXTRA_LOGIN)?.takeIf { it.isNotBlank() }
        if (login == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        useLightSystemBars()
        setContent {
            AppTheme {
                BackHandler { finishAndRemoveTask() }
                GameScreen(playerName = login, onExit = { finishAndRemoveTask() })
            }
        }
    }

    companion object {
        const val EXTRA_LOGIN = "ua.cn.stu.tictactoe.LOGIN"
    }
}
