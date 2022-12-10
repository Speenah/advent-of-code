import java.io.File

val xCycle = mutableListOf<Int>()
xCycle += 1

File("input.txt").forEachLine {
    val inst = it.split(" ")
    val x = xCycle.last()

    when (inst[0]) {
        "addx" -> {
            xCycle += x
            xCycle += x + inst[1].toInt()
        }
        "noop" -> xCycle += x
    }
}

val cyclesToFind = listOf(20, 60, 100, 140, 180, 220)

var strength = 0
for (i in cyclesToFind) {
    strength += i * xCycle[i - 1]
}
println(strength)

val crt = mutableListOf<Char>()

for (i in 0 until xCycle.size - 1) {
    val x = xCycle[i]
    val spriteRange = (x - 1)..(x + 1)
    if (i % 40 in spriteRange) {
        crt += '#'
    } else {
        crt += '.'
    }
}


crt.chunked(40).forEach {
    println(it.joinToString(""))
}
