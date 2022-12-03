import java.io.File
import kotlin.math.min

val ROCK = 1
val PAPER = 2
val SCISSORS = 3

var score = 0
File("input.txt").forEachLine {
    val moves = it.split(' ')
        .map { it.toScore() }

    val them = moves[0]
    val mine = moves[1]

    val roundScore = mine + winBonus(them, mine)

    score += roundScore

    println("Moves $moves, Win ${winBonus(them, mine)}, Score $roundScore")
}

fun String.toScore(): Int = when (this) {
    "A", "X" -> ROCK
    "B", "Y" -> PAPER
    "C", "Z" -> SCISSORS
    else -> error("Unknown move")
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
