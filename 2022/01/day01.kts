import java.io.File

var buffer = 0
val totals = mutableListOf<Int>()

File("input.txt").forEachLine {
    if (it != "") {
        buffer += it.toInt()
    } else {
        totals += buffer
        buffer = 0
    }
}

totals.sortDescending()

val max = totals.first()
val topThreeTotal = totals.take(3).sum()

println("Max = $max")
println("Top Three = $topThreeTotal")
