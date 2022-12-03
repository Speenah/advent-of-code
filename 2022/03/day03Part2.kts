import java.io.File

var sum = 0
File("input.txt").useLines { lines ->
    lines.chunked(3).forEach { group ->
        val (set1, set2, set3) = group.map { it.toSet() }
        for (c in set1) {
            if (c in set2 && c in set3) {
                sum += c.priority()
            }
        }
    }
}

println(sum)

fun Char.priority() =
    if (this.isUpperCase()) {
        this.code - 38
    } else {
        this.code - 96
    }
