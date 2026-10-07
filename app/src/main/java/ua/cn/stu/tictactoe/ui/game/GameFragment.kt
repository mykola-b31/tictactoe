package ua.cn.stu.tictactoe.ui.game

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import ua.cn.stu.tictactoe.AppTheme
import ua.cn.stu.tictactoe.R
import ua.cn.stu.tictactoe.service.GameMoveService

class GameFragment : Fragment() {
    private var moveService by mutableStateOf<GameMoveService?>(null)
    private var bindingRegistered = false

    companion object {
        private const val ARG_PLAYER_NAME = "player_name"
        private const val NO_MOVE = -1
        private const val TAG = "GameFragment"

        fun newInstance(playerName: String): GameFragment = GameFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PLAYER_NAME, playerName)
            }
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            moveService = (binder as GameMoveService.GameMoveBinder).service
            Log.d(TAG, "GameMoveService connected")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            moveService = null
            Log.d(TAG, "GameMoveService disconnected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val playerName = requireArguments().getString(ARG_PLAYER_NAME)
            ?.takeIf { it.isNotBlank() }
            ?: getString(R.string.default_player_name)

        return ComposeView(requireContext()).apply {
            id = R.id.game_compose_view
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                AppTheme {
                    val service = moveService
                    GameScreen(
                        playerName = playerName,
                        aiAvailable = service != null,
                        onRequestAiMove = { board ->
                            service?.calculateNextMove(board) ?: NO_MOVE
                        },
                        onExit = { requireActivity().finishAndRemoveTask() }
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!bindingRegistered) {
            bindingRegistered = requireContext().bindService(
                Intent(requireContext(), GameMoveService::class.java),
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    override fun onStop() {
        if (bindingRegistered) {
            requireContext().unbindService(serviceConnection)
            bindingRegistered = false
            moveService = null
        }
        super.onStop()
    }
}
