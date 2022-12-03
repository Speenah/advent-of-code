import java.io.File

val ROCK = 1
val PAPER = 2
val SCISSORS = 3

val LOOSE = 1
val DRAW = 2
val WIN = 3

var score = 0
File("input.txt").forEachLine {
    val moves = it.split(' ')
        .map { Move.fromString(it) }

    val them = moves[0]
    val mine = counterMove(them, moves[1].score)

    val roundScore = mine.score + winBonus(them.score, mine.score)

    score += roundScore
}

fun counterMove(theirMove: Move, desiredResult: Int): Move = when (desiredResult) {
    LOOSE -> theirMove.decreased()
    DRAW -> theirMove.copy()
    WIN -> theirMove.increased()
    else -> error("Unexpected counter")
}

data class Move(val score: Int) {
    operator fun compareTo(that: Move): Int {
        if (this == ROCK && that == SCISSORS) {
            return 1
        }

        return this.score.compareTo(that.score)
    }

    fun increased(): Move {
        var nextScore = score + 1
        if (nextScore > 3) nextScore = 1
        return copy(score = nextScore)
    }

    fun decreased(): Move {
        var nextScore = score - 1
        if (nextScore < 1) nextScore = 3
        return copy(score = nextScore)
    }

    companion object {
        val ROCK = Move(1)
        val PAPER = Move(2)
        val SCISSORS = Move(3)

        fun fromString(letter: String): Move = when (letter) {
            "A", "X" -> ROCK
            "B", "Y" -> PAPER
            "C", "Z" -> SCISSORS
            else -> error("Unknown move")
        }
    }
}

fun winBonus(theirMove: Int, myMove: Int): Int {
    if (theirMove == SCISSORS && myMove == ROCK) {
        return 6
    }
    if (theirMove == ROCK && myMove == SCISSORS) {
        return 0
    }

    if (theirMove == myMove) {
        return 3
    } else if (theirMove > myMove) {
        return 0
    } else {
        return 6
    }
}

println(score)
