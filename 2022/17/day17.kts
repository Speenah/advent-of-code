import java.io.File
import kotlin.math.max
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource

val jets = File("input.txt")
    .readText()
    .split("")
    .filter { it in setOf(">", "<") }
val nJets = jets.size

// region Pieces

// Piece initial positions defined as if spawning on blank board
// -
val heroH = Piece(
    setOf(
        Pos(2, 4),
        Pos(3, 4),
        Pos(4, 4),
        Pos(5, 4)
    )
)

// +
val teewee = Piece(
    setOf(
        Pos(3, 4),
        Pos(2, 5),
        Pos(3, 5),
        Pos(4, 5),
        Pos(3, 6)
    )
)

// J
val ricky = Piece(
    setOf(
        Pos(2, 4),
        Pos(3, 4),
        Pos(4, 4),
        Pos(4, 5),
        Pos(4, 6)
    )
)

// |
val heroV = Piece(
    setOf(
        Pos(2, 4),
        Pos(2, 5),
        Pos(2, 6),
        Pos(2, 7)
    )
)

val smashboy = Piece(
    setOf(
        Pos(2, 4),
        Pos(3, 4),
        Pos(2, 5),
        Pos(3, 5)
    )
)
// endregion

val pieces = listOf(heroH, teewee, ricky, heroV, smashboy)
val nPieces = 5L

val maxRocks = 1_000_000_000_000

val maxJetsToFindCycle = nPieces * nJets

val xRange = 0..6
val occupiedSpace = hashSetOf<Pos>()

var currMaxY = 0L
var jet = 0

// nPiece,nJet to nRock,Y
val cycleCache = mutableMapOf<Pair<Int, Int>, Pair<Long,Long>>()

for (nRock in 0L until maxRocks) {
    val nPiece = (nRock % nPieces).toInt()
    val nJet = jet % nJets

    val nPieceJet = nPiece to nJet
    if (cycleCache.containsKey(nPieceJet)) {
        val (cacheRock, cacheY) = cycleCache.getValue(nPieceJet)
        val cycleDiffY = (maxRocks - nRock) / (nRock - cacheRock)
        val cycleDiffRem = (maxRocks - nRock) % (nRock - cacheRock)

        if (cycleDiffRem == 0L) {
            val finalY = currMaxY + ((currMaxY - cacheY) * cycleDiffY)
            println(finalY)
            break
        }
    } else {
        cycleCache[nPieceJet] = nRock to currMaxY
    }

    val piece = pieces[nPiece].copy()
    piece.moveUp(currMaxY)

    do {
        val direction = jets[jet++ % nJets]
        piece.moveX(direction)
    } while (piece.movedDown())

    occupiedSpace += piece.usedPos
    currMaxY = max(currMaxY, piece.maxY)
}

//printBoard(occupiedSpace)

/////////////////////////

fun printBoard(space: Set<Pos>) {
    val rows = space.sortedByDescending { it.y }.groupBy { it.y }

    val maxRow = rows.keys.max()

    for (nRow in maxRow downTo 1) {
        val row = rows[nRow]

        if (row !== null) {
            val xs = row.map { it.x }
            print("|")
            for (i in xRange) {
                if (i.toLong() in xs) print("#") else print(".")
            }
            println("|")
        } else {
            println("|.......|")
        }
    }
    println("+-------+\n")
}

data class Pos(val x: Long, val y: Long)
data class Piece(var usedPos: Set<Pos>) {
    val minX: Long
        get() = usedPos.minBy { it.x }.x
    val minY: Long
        get() = usedPos.minBy { it.y }.y
    val maxX: Long
        get() = usedPos.maxBy { it.x }.x
    val maxY: Long
        get() = usedPos.maxBy { it.y }.y

    fun moveUp(n: Long) {
        usedPos = usedPos.map {
            Pos(it.x, it.y + n)
        }.toSet()
    }

    fun moveDown() {
        usedPos = usedPos.map {
            Pos(it.x, it.y - 1)
        }.toSet()
    }

    fun moveLeft() {
        if (minX.toInt() == xRange.first) return

        val nextPos = usedPos.map {
            Pos(it.x - 1, it.y)
        }

        val nextCollides = nextPos.any { occupiedSpace.contains(it) } || nextPos.any { it.y <= 0 }
        if (!nextCollides) {
            usedPos = nextPos.toSet()
        }
    }

    fun moveRight() {
        if (maxX.toInt() == xRange.last) return

        val nextPos = this.copy().usedPos.map {
            Pos(it.x + 1, it.y)
        }

        val nextCollides = nextPos.any { occupiedSpace.contains(it) } || nextPos.any { it.y <= 0 }
        if (!nextCollides) {
            usedPos = nextPos.toSet()
        }
    }

    fun movedDown(): Boolean {
        val nextPos = this.copy().usedPos.map {
            Pos(it.x, it.y - 1)
        }

        val nextCollides = nextPos.any { occupiedSpace.contains(it) } || nextPos.any { it.y <= 0 }

        if (!nextCollides) {
            this.moveDown()
            return true
        } else {
            return false
        }
    }

    fun moveX(direction: String) {
        when (direction) {
            ">" -> moveRight()
            "<" -> moveLeft()
            else -> error("Unknown direction $direction")
        }
    }
}
