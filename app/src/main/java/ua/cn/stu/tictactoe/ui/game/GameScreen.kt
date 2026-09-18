package ua.cn.stu.tictactoe.ui.game

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ua.cn.stu.tictactoe.R
import ua.cn.stu.tictactoe.game.TicTacToeGame
import ua.cn.stu.tictactoe.game.TicTacToeGame.Result

@Composable
fun GameScreen(playerName: String, onExit: () -> Unit) {
    var board by rememberSaveable { mutableStateOf(TicTacToeGame.NEW_BOARD) }
    val result = TicTacToeGame.result(board)
    val playerTurn = TicTacToeGame.isPlayerTurn(board)

    LaunchedEffect(board) {
        if (result == Result.PLAYING && !playerTurn) {
            val snapshot = board
            delay(300)
            val index = withContext(Dispatchers.Default) { TicTacToeGame.findBestMove(snapshot) }
            if (board == snapshot && index >= 0) board = TicTacToeGame.move(snapshot, index, TicTacToeGame.AI)
        }
    }
    val spacing = dimensionResource(R.dimen.spacing)
    val padding = dimensionResource(R.dimen.screen_padding)
    val maxBoard = dimensionResource(R.dimen.board_size)
    val status = when {
        result != Result.PLAYING -> R.string.game_finished
        playerTurn -> R.string.player_turn
        else -> R.string.ai_turn
    }
    val restart = { board = TicTacToeGame.NEW_BOARD }
    val play: (Int) -> Unit = { index->
        if (playerTurn) board = TicTacToeGame.move(board, index, TicTacToeGame.PLAYER)
    }

    BoxWithConstraints(Modifier.fillMaxSize().safeDrawingPadding().padding(padding)) {
        if (maxWidth > maxHeight) {
            val boardSize = minOf(maxBoard, maxHeight, (maxWidth - spacing) / 2).coerceAtLeast(144.dp)
            Row(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GameBoard(board, playerTurn && result == Result.PLAYING, play, Modifier.size(boardSize))
                GameControls(playerName, status, restart, onExit, Modifier.widthIn(max = dimensionResource(R.dimen.content_width)).weight(1f))
            }
        } else {
            val boardSize = minOf(maxBoard, maxWidth, (maxHeight - 220.dp).coerceAtLeast(144.dp))
            Column(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterVertically)
            ) {
                Text(stringResource(R.string.player_name, playerName), style = MaterialTheme.typography.titleLarge)
                Text(stringResource(status))
                GameBoard(board, playerTurn && result == Result.PLAYING, play, Modifier.size(boardSize))
                GameButtons(restart, onExit, Modifier.widthIn(max = maxBoard).fillMaxWidth())
            }
        }
    }

    if (result != Result.PLAYING) {
        val message = when (result) {
            Result.PLAYER_WON -> R.string.player_won
            Result.AI_WON -> R.string.ai_won
            else -> R.string.draw
        }
        AlertDialog(
            onDismissRequest = {},
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            title = { Text(stringResource(R.string.result_title)) },
            text = { Text(stringResource(message)) },
            confirmButton = { TextButton(onClick = onExit) { Text(stringResource(R.string.ok)) } },
            dismissButton = { TextButton(onClick = restart) { Text(stringResource(R.string.again)) } }
        )
    }
}

@Composable
private fun GameControls(playerName: String, status: Int, onRestart: () -> Unit, onExit: () -> Unit, modifier: Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing))) {
        Text(stringResource(R.string.player_name, playerName), style = MaterialTheme.typography.titleLarge)
        Text(stringResource(status))
        GameButtons(onRestart, onExit, Modifier.fillMaxWidth())
    }
}

@Composable
private fun GameButtons(onRestart: () -> Unit, onExit: () -> Unit, modifier: Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_spacing))) {
        Button(onClick = onRestart, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.restart))
        }
        OutlinedButton(onClick = onExit, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.exit))
        }
    }
}

@Composable
private fun GameBoard(board: String, enabled: Boolean, onCellClick: (Int) -> Unit, modifier: Modifier) {
    Column(modifier) {
        repeat(3) { row ->
            Row(Modifier.weight(1f)) {
                repeat(3) { column ->
                    val index = row * 3 + column
                    val mark = board[index]
                    val description = stringResource(R.string.cell_description, row + 1, column + 1,
                        if (mark == TicTacToeGame.EMPTY) stringResource(R.string.empty_cell) else mark.toString())
                    Box(
                        modifier = Modifier.weight(1f).fillMaxHeight()
                            .border(1.dp, MaterialTheme.colorScheme.outline)
                            .semantics { contentDescription = description }
                            .clickable(enabled = enabled && mark == TicTacToeGame.EMPTY, role = Role.Button) { onCellClick(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (mark == TicTacToeGame.EMPTY) "" else mark.toString(), fontSize = 36.sp)
                    }
                }
            }
        }
    }
}
