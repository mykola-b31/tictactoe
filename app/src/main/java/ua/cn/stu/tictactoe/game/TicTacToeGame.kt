package ua.cn.stu.tictactoe.game

object TicTacToeGame {
    const val EMPTY = '.'
    const val PLAYER = 'X'
    const val AI = 'O'
    const val NEW_BOARD = "........."

    enum class Result { PLAYING, PLAYER_WON, AI_WON, DRAW }

    private val lines = arrayOf(
        intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8), // по горизонталі
        intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8), // по вертикалі
        intArrayOf(0, 4, 8), intArrayOf(2, 4, 6) // по діагоналі
    )

    fun result(board: String): Result {
        require(board.length == 9 && board.all { it == EMPTY || it == PLAYER || it == AI })
        for ((a, b, c) in lines) {
            if (board[a] != EMPTY && board[a] == board[b] && board[a] == board[c]) {
                return if (board[a] == PLAYER) Result.PLAYER_WON else Result.AI_WON
            }
        }
        return if (EMPTY in board) Result.PLAYING else Result.DRAW
    }

    fun isPlayerTurn(board: String): Boolean = board.count { it == PLAYER } == board.count { it == AI }

    fun move(board: String, index: Int, mark: Char): String {
        if (result(board) != Result.PLAYING || index !in board.indices || board[index] != EMPTY) return board
        val expected = if (isPlayerTurn(board)) PLAYER else AI
        return if (mark == expected) board.replaceRange(index, index + 1, mark.toString()) else board
    }

    fun findBestMove(board: String): Int {
        if (result(board) != Result.PLAYING || isPlayerTurn(board)) return -1
        return board.indices.filter { board[it] == EMPTY }.maxByOrNull { index ->
            minimax(board.replaceRange(index, index + 1, AI.toString()), aiTurn = false, depth = 0)
        } ?: -1
    }

    private fun minimax(board: String, aiTurn: Boolean, depth: Int): Int {
        when (result(board)) {
            Result.AI_WON -> return 10 - depth
            Result.PLAYER_WON -> return depth - 10
            Result.DRAW -> return 0
            Result.PLAYING -> Unit
        }
        val mark = if (aiTurn) AI else PLAYER
        val scores = board.indices.filter { board[it] == EMPTY }.map { index ->
            minimax(board.replaceRange(index, index + 1, mark.toString()), !aiTurn, depth + 1)
        }
        return if (aiTurn) scores.max() else scores.min()
    }
}
