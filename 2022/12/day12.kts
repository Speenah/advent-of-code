import java.io.File
import kotlin.math.abs


typealias Pos = Pair<Int, Int>

data class Step(val pos: Pos, val nSteps: Int = 0)

val input = File("input.txt").readLines()
    .map { it.toCharArray().toList() }

val xRange = 0 until input.size
val yRange = 0 until input[0].size

lateinit var start: Pos
lateinit var end: Pos
for (x in xRange) {
    for (y in yRange) {
        when (input[x][y]) {
            'S' -> start = x to y
            'E' -> end = x to y
        }
    }
}

println("$start to $end")

println(
    bfs(start, { it == end }) { pos, nextPos ->
        pos.heightDiff(nextPos) <= 1
    }
)

println(
    bfs(end, { input.pos(it) == 'a' }) { pos, nextPos ->
        pos.heightDiff(nextPos) >= -1
    }
)

////////////////////////
fun bfs(start: Pos, isEnd: (Pos) -> Boolean, isValid: (Pos, Pos) -> Boolean): Int {
    val steps = ArrayDeque<Step>().apply {
        add(Step(start))
    }
    
    val visited = hashSetOf<Pos>()
    
    while (steps.isNotEmpty()) {
        val step = steps.removeFirst()
        val pos = step.pos
        
        if (visited.contains(pos)) continue
       
        if (isEnd(pos)) {
            return step.nSteps
        }

        for (nextPos in step.pos.nextPositions()) {
            if (isValid(pos, nextPos)) {
                steps.add(Step(nextPos, step.nSteps + 1))
            }
        }

        visited.add(step.pos)
    }

    return Int.MAX_VALUE
}

fun Pos.nextPositions() = listOf<Pos>(
    first + 1 to second + 0,
    first + 0 to second + 1,
    first - 0 to second - 1,
    first - 1 to second - 0
).filter {
    it.first in xRange && it.second in yRange
}

fun Pos.heightDiff(that: Pos): Int {
    val thisHeight = this.mapToHeight()
    val thatHeight = that.mapToHeight()

    return thatHeight.code - thisHeight.code
}

fun Pos.mapToHeight() = when (input.pos(this)) {
    'S' -> 'a'
    'E' -> 'z'
    else -> input.pos(this)
}

fun List<List<Char>>.pos(pos: Pos): Char = this[pos.first][pos.second]
