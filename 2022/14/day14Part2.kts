import java.io.File

data class Pos(val x: Int, val y: Int) {
    fun down() = copy(y = y + 1)
    fun downLeft() = copy(x = x - 1, y = y + 1)
    fun downRight() = copy(x = x + 1, y = y + 1)
}

val filled = hashSetOf<Pos>()
File("input.txt").forEachLine { line ->
    val rockPos =  line.split(" -> ")
    for (i in 1 until rockPos.size) {
        val rock1 = rockPos[i - 1].split(",").map { it.toInt() }
        val rock2 = rockPos[i].split(",").map { it.toInt() }

        val xs = listOf(rock1[0], rock2[0]).sorted()
        val ys = listOf(rock1[1], rock2[1]).sorted()

        val rockPos1 = Pos(xs[0], ys[0])
        val rockPos2 = Pos(xs[1], ys[1])

        when {
            xs.distinct().size == 1 -> {
                for (i in ys[0]..ys[1])
                    filled += Pos(xs[0], i)
            }
            ys.distinct().size == 1 -> {
                for (i in xs[0]..xs[1])
                    filled += Pos(i, ys[0])
            }
        }
    }
}

val maxY = filled.maxBy { it.y }.y
val floorY = maxY + 2

for (floorX in 0..1000) {
    filled += Pos(floorX, floorY)
}

val sandSpawn = Pos(500, 0)

var sandCount = 0
var sandPos = sandSpawn.copy()
do {
    when {
        !filled.contains(sandPos.down()) -> sandPos = sandPos.down()
        !filled.contains(sandPos.downLeft()) -> sandPos = sandPos.downLeft()
        !filled.contains(sandPos.downRight()) -> sandPos = sandPos.downRight()
        else -> {
            filled += sandPos.copy()
            sandPos = sandSpawn.copy()
            sandCount++
        }
    }
} while (!filled.contains(sandSpawn))

println(sandCount)
