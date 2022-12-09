import java.io.File
import kotlin.math.abs
import kotlin.math.sign

data class Knot(var x: Int, var y: Int) {
    val pos: Pair<Int, Int>
        get() = x to y

    fun move(direction: String) =
        when (direction) {
            "R" -> x++
            "U" -> y++
            "L" -> x--
            "D" -> y--
            else -> error("Unknown direction")
        }

    fun isTouching(that: Knot) =
        abs(this.x - that.x) <= 1 && abs(this.y - that.y) <= 1

    fun follow(that: Knot) {
        if (isTouching(that)) return

        val dx = that.x - this.x
        val dy = that.y - this.y

        this.x += dx.sign
        this.y += dy.sign
    }
}

val nKnots = 10
val rope = List(nKnots) { Knot(0, 0) }

val visited = mutableSetOf<Pair<Int, Int>>()

File("input.txt").forEachLine { line ->
    val split = line.split(" ")
    val direction = split[0]
    val count = split[1].toInt()

    repeat(count) {
        rope.first().move(direction)
        for (i in 1 until rope.size) {
            val knot = rope[i]
            knot.follow(rope[i - 1])
        }
        visited += rope.last().pos
    }
}

println(visited.size)
