import java.io.File

var sum = 0
File("input.txt").forEachLine {
    val mid = it.length / 2
    val first= it.slice(0 until mid)
    val second = it.slice(mid until it.length)

    for (c1 in first) {
        for (c2 in second) {
            if (c1 == c2) {
                val priority = c1.toPriority()
                println(c1 + " " + priority)
                sum += c1.toPriority()
                return@forEachLine
            }
        }
    }
}

println(sum)

fun Char.toPriority() =
    if (this.isUpperCase()) {
        this.code - 38
    } else {
        this.code - 96
    }
