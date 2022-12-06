import java.io.File

val input = File("input.txt").readText()

val minDistinct = 14

for (i in minDistinct until input.length) {
    val s = input.substring((i - minDistinct) until i)
    if (s.chars().distinct().toArray().size == minDistinct) {
        println(i)
        break
    }
}
