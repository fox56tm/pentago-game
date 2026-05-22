package service

import model.Game
import model.Move

class DefaultRules : GameRuleEngine {
    override fun validateMove(
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

    override fun applyRotation(
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
        game.nextMoveColor()
    }

    override fun checkWinner(game: Game): String? {
        val b = game.board
        val lines = mutableListOf<List<Pair<Int, Int>>>()

        for (r in 0..5) {
            for (c in 0..1)
                lines.add((0..4).map { r to c + it })
        }
        for (c in 0..5) {
            for (r in 0..1)
                lines.add((0..4).map { r + it to c })
        }
        for (r in 0..1) {
            for (c in 0..1)
                lines.add((0..4).map { r + it to c + it })
        }
        for (r in 0..1) {
            for (c in 0..1)
                lines.add((0..4).map { r + it to c + 4 - it })
        }
        var whiteWins = false
        var blackWins = false
        for (l in lines) {
            val values = l.map { (r, c) -> b[r][c] }
            if (values.all { it == 1 }) whiteWins = true
            if (values.all { it == 2 }) blackWins = true
        }
        val isFull = b.all { row -> row.all { it != 0 } }

        if (whiteWins && blackWins) return "DRAW"
        if (whiteWins) return "W"
        if (blackWins) return "B"
        if (isFull) return "DRAW"
        return null
    }
}
