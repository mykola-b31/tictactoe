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

        val availableMoves = board.indices.filter { board[it] == EMPTY }

        findWinningMove(board, AI)?.let { return it }

        findWinningMove(board, PLAYER)?.let { return it }

        /*if (board[4] == EMPTY) {
            return 4
        }*/

        return availableMoves.randomOrNull() ?: -1
    }

    private fun findWinningMove(board: String, mark: Char): Int? {
        val expectedResult = when (mark) {
            AI -> Result.AI_WON
            PLAYER -> Result.PLAYER_WON
            else -> return null
        }

        return board.indices
            .filter { board[it] == EMPTY }
            .firstOrNull { index ->
                val nextBoard = board.replaceRange(
                    index,
                    index + 1,
                    mark.toString()
                )

                result(nextBoard) == expectedResult
            }
    }
}
