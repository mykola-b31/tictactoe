package ua.cn.stu.tictactoe.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.cn.stu.tictactoe.game.TicTacToeGame

class GameMoveService : Service() {
    private val binder = GameMoveBinder()

    companion object {
        private const val TAG = "GameMoveService"
    }

    inner class GameMoveBinder : Binder() {
        val service: GameMoveService
            get() = this@GameMoveService
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    suspend fun calculateNextMove(board: String): Int =
        withContext(Dispatchers.Default) {
            TicTacToeGame.findBestMove(board).also { move ->
                Log.d(TAG, "Calculated move $move for board $board")
            }
        }
}
