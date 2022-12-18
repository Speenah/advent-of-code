import java.io.File
import java.util.ArrayDeque

val cubes = File("input.txt").readLines().map {
    val dim = it.split(",").map { it.toInt() }
    Cube(dim[0], dim[1], dim[2])
}

val exposedSides = cubes.map { cube ->
    6 - cube.adjacentCubes().size
}

println(exposedSides.sum())

val outerBound1 = Cube(cubes.minOf { it.x - 1 }, cubes.minOf { it.y - 1 }, cubes.minOf { it.z - 1 })
val outerBound2 = Cube(cubes.maxOf { it.x + 1 }, cubes.maxOf { it.y + 1 }, cubes.maxOf { it.z + 1 })

var outerSides = 0
val outerCubes = ArrayDeque<Cube>().apply {
    add(outerBound1)
}

val visited = hashSetOf<Cube>()

while (outerCubes.isNotEmpty()) {
    val cube = outerCubes.removeFirst()
    if (cube in visited) continue

    cube.neighboringSpace().filter { it.inBounds() }
        .forEach {
            if (it in cubes) outerSides++ else outerCubes += it
        }
    visited += cube
}

println(outerSides)

/////////////////////////////////////

data class Cube(val x: Int, val y: Int, val z: Int) {
    fun neighboringSpace(): List<Cube> =
        listOf(
            copy(x = x + 1),
            copy(x = x - 1),
            copy(y = y + 1),
            copy(y = y - 1),
            copy(z = z + 1),
            copy(z = z - 1)
        )

    fun adjacentCubes(): List<Cube> = neighboringSpace().filter { it in cubes }
}

fun Cube.inBounds() = x in outerBound1.x..outerBound2.x
    && y in outerBound1.y..outerBound2.y
    && z in outerBound1.z..outerBound2.z
