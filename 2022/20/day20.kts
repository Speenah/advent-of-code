import java.io.File

val input = File("input.txt").readLines().map { it.toInt() }

fun decrypt(key: Int = 1, mixCount: Int = 1): Long {
    // Pair with original idx to ensure moving the correct number
    val original = input.mapIndexed { idx, i ->
        Pair(idx, i.toLong() * key)
    }

    val moved = original.toMutableList()
    repeat(mixCount) {
        original.forEach { p ->
            val idx = moved.indexOf(p)
            moved.removeAt(idx)
            moved.add((idx + p.second).mod(moved.size), p)
        }
    }

    val mixed = moved.map { it.second }
    val idx0 = mixed.indexOf(0)

    return listOf(1000, 2000, 3000).sumOf { groveIdx ->
        mixed[(groveIdx + idx0) % mixed.size]
    }
}

println(decrypt())
println(decrypt(811_589_153, 10))
