import java.io.File
import kotlin.math.max

var visible = 0
var maxScenic = 0
val grid = mutableListOf<List<Int>>()

File("input.txt").forEachLine { line ->
    grid.add(line.map { it.digitToInt() })
}

for (x in 1 until grid.size - 1) {
    val row = grid[x]
    for (y in 1 until row.size - 1) {
        val col = grid.map { it[y] }

        val vectors = listOf(
            row.take(y).reversed(), // left
            row.takeLast(row.size - 1 - y), // right
            col.take(x).reversed(), // up
            col.takeLast(col.size - 1 - x) // down
        )

        val tree = row[y]
        val notVisible = !vectors.any { tree > it.max() }
        if (notVisible) continue
        visible++

        var scenicScore = 1
        vectors.forEach { v ->
            scenicScore *=
                if (tree <= v.max()) {
                    // count of trees until blocked
                    v.indexOf(v.filter { it >= tree }.min()) + 1
                } else {
                    v.size
                }
        }

        if (scenicScore > maxScenic) maxScenic = scenicScore
    }
}

visible += (2 * grid.size) + (2 * (grid[0].size - 2))

println(visible)
println(maxScenic)

