import java.io.File
import java.util.Stack
/*
        [C] [B] [H]
[W]     [D] [J] [Q] [B]
[P] [F] [Z] [F] [B] [L]
[G] [Z] [N] [P] [J] [S] [V]
[Z] [C] [H] [Z] [G] [T] [Z]     [C]
[V] [B] [M] [M] [C] [Q] [C] [G] [H]
[S] [V] [L] [D] [F] [F] [G] [L] [F]
[B] [J] [V] [L] [V] [G] [L] [N] [J]
 1   2   3   4   5   6   7   8   9
*/
// transposed
val stacks = """
    WPGZVSB
    FZCBVJ
    CDZNHMLV
    BJFPZMDL
    HQBJHCFV
    BLSTQFG
    VZCGL
    GLN
    CHFJ
""".trimIndent().split("\n").map {
    Stack<Char>().apply {
        it.reversed().forEach { c -> push(c) }
    }
}

// input file is only move instructions, didn't want to parse the stacks
File("input.txt").forEachLine {
    val (amount, from, to) = it.replace("[a-z]".toRegex(), "").trim().split("  ").map { it.toInt() }
    val fromStack = stacks[from - 1]
    val toStack = stacks[to - 1]

    val buffer = Stack<Char>()

    repeat(amount) {
        buffer.push(fromStack.pop())
    }
    repeat(amount) {
        toStack.push(buffer.pop())
    }
}

stacks.forEach { print(it.peek()) }
