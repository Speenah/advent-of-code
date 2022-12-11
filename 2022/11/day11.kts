import java.io.File
import java.lang.Exception
import java.math.BigInteger

data class Monkey(
    val id: Int,
    val itemWorry: MutableList<Long>,
    val operation: List<String>,
    val testDividend: Long,
    val throwsTo: Pair<Int, Int>
)

val monkeys = mutableListOf<Monkey>()

var monkeyId = -1
var startingItems = mutableListOf<Long>()
var operation = listOf<String>()
var dividend = 0L
var trueMonkey = -1
var falseMonkey = -1
File("input.txt").forEachLine {
    when {
        it.contains("Monkey") -> monkeyId = it.get(7).digitToInt()
        it.contains("  Starting items: ") -> startingItems = it.split(": ").last()
            .split(", ")
            .map { it.toLong() }
            .toMutableList()

        it.contains("  Operation: ") -> {
            val op = it.replace("  Operation: new = old ", "")
            operation = op.split(" ")
        }

        it.contains("  Test") -> dividend = it.split(" ").last().toLong()

        it.contains("    If true") -> trueMonkey = it.split(" ").last().toInt()

        it.contains("    If false") -> falseMonkey = it.split(" ").last().toInt()

        else -> monkeys += Monkey(
            id = monkeyId,
            itemWorry = startingItems,
            operation = operation,
            testDividend = dividend,
            throwsTo = trueMonkey to falseMonkey
        )
    }
}

val inspections = hashMapOf<Int, Int>()
val lcd = monkeys.map { it.testDividend }.reduce { acc, l -> acc * l }

fun doRound() {
    monkeys.forEach { monkey ->
        val op = monkey.operation.first()
        monkey.itemWorry.forEach { item ->
            val opS =
                monkey.operation.last().toLongOrNull() ?: item
            var newWorry = when (op) {
                "*" -> BigInteger.valueOf(item * opS).longValueExact() % lcd // 3
                "+" -> BigInteger.valueOf(item + opS).longValueExact() % lcd // 3
                else -> error("Unknown op")
            }

            if (newWorry % monkey.testDividend.toLong() == 0L) {
                monkeys[monkey.throwsTo.first].itemWorry += newWorry
            } else {
                monkeys[monkey.throwsTo.second].itemWorry += newWorry
            }
            inspections[monkey.id] = (inspections[monkey.id] ?: 0) + 1
        }
        monkey.itemWorry.clear()
    }
}

repeat(10_000) {
    doRound()
}

monkeys.forEach { println(it) }
println(inspections)
val monkeyBusiness = inspections.values.sortedDescending().take(2).map { it.toLong() }.reduce { acc, i -> acc * i }
println(monkeyBusiness)
