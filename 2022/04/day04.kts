import java.io.File

var fullOverlaps = 0
var partialOverlaps = 0
File("input.txt").forEachLine {
    val pairs = it.split(",").map {
        val ends = it.split("-").map { it.toInt() }
        ends[0]..ends[1]
    }

    if (pairs[0].within(pairs[1]) || pairs[1].within(pairs[0])) {
        fullOverlaps++
    }

    if (pairs[0].overlaps(pairs[1])) {
        partialOverlaps++
    }
}

println(fullOverlaps)
println(partialOverlaps)

fun IntRange.within(that: IntRange) =
    this.start >= that.start && this.endInclusive <= that.endInclusive

fun IntRange.overlaps(that: IntRange) =
    any {
        that.contains(it)
    }
