package service

import model.Game
import model.Move

class GameRuleEngine {
    fun validateMove(
        game: Game,
        move: Move
    ): Boolean {
        if (game.status != "ACTIVE") {
            println("Game was ended with status: ${game.status}")
            return false
        }

        if (move.x !in 0..5 || move.y !in 0..5) {
            println("Invalid board places: (${move.x}, ${move.y})")
            return false
        }

        if (game.getCell(move.x, move.y) != 0) {
            println("Place (${move.x}, ${move.y}) is not empty")
            return false
        }

        if (move.quadrant !in 0..3) {
            println("Quadrant must be 0-3, not: ${move.quadrant}")
            return false
        }

        if (move.rotation !in listOf("L", "R")) {
            println("Rotation only 'L' or 'R', not: '${move.rotation}'")
            return false
        }
        return true
    }

    fun applyRotation(
        game: Game,
        quadrant: Int,
        direction: String
    ) {
        var rowOff = 0
        var colOff = 0
        if (quadrant == 1) colOff = 3
        if (quadrant == 2) rowOff = 3
        if (quadrant == 3) {
            rowOff = 3
            colOff = 3
        }
        val sub = Array(3) { Array(3) { 0 } }
        for (r in 0..2) {
            for (c in 0..2) {
                sub[r][c] = game.board[rowOff + r][colOff + c]
            }
        }
        val rotated = Array(3) { Array(3) { 0 } }
        for (r in 0..2) {
            for (c in 0..2) {
                if (direction == "R") {
                    rotated[c][2 - r] = sub[r][c]
                } else {
                    rotated[2 - c][r] = sub[r][c]
                }
            }
        }
        for (r in 0..2) {
            for (c in 0..2) {
                game.board[rowOff + r][colOff + c] = rotated[r][c]
            }
        }
    }

    fun checkWinner(game: Game): String? {
        val b = game.board
        var whiteWins = false
        var blackWins = false
        for (r in 0..5) {
            for (startC in 0..1) {
                if (b[r][startC] != 0 &&
                    b[r][startC] == b[r][startC + 1] &&
                    b[r][startC] == b[r][startC + 2] &&
                    b[r][startC] == b[r][startC + 3] &&
                    b[r][startC] == b[r][startC + 4]
                ) {
                    if (b[r][startC] == 1) whiteWins = true
                    if (b[r][startC] == 2) blackWins = true
                }
            }
        }
        for (c in 0..5) {
            for (startR in 0..1) {
                if (b[startR][c] != 0 &&
                    b[startR][c] == b[startR + 1][c] &&
                    b[startR][c] == b[startR + 2][c] &&
                    b[startR][c] == b[startR + 3][c] &&
                    b[startR][c] == b[startR + 4][c]
                ) {
                    if (b[startR][c] == 1) whiteWins = true
                    if (b[startR][c] == 2) blackWins = true
                }
            }
        }
        for (startR in 0..1) {
            for (startC in 0..1) {
                if (b[startR][startC] != 0 &&
                    b[startR][startC] == b[startR + 1][startC + 1] &&
                    b[startR][startC] == b[startR + 2][startC + 2] &&
                    b[startR][startC] == b[startR + 3][startC + 3] &&
                    b[startR][startC] == b[startR + 4][startC + 4]
                ) {
                    if (b[startR][startC] == 1) whiteWins = true
                    if (b[startR][startC] == 2) blackWins = true
                }
                val sc = startC + 4
                if (b[startR][sc] == b[startR + 1][sc - 1] &&
                    b[startR][sc] == b[startR + 2][sc - 2] &&
                    b[startR][sc] == b[startR + 3][sc - 3] &&
                    b[startR][sc] == b[startR + 4][sc - 4]
                ) {
                    if (b[startR][sc] == 1) whiteWins = true
                    if (b[startR][sc] == 2) blackWins = true
                }
            }
        }
        var isFull = true
        for (r in 0..5) {
            for (c in 0..5) {
                if (b[r][c] == 0) isFull = false
            }
        }
        if (whiteWins && blackWins) return "DRAW"
        if (whiteWins) return "W"
        if (blackWins) return "B"
        if (isFull) return "DRAW"
        return null
    }
}
