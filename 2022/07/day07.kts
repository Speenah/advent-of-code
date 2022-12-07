import java.io.File

data class Dir(
    val name: String,
    var fileSize: Int = 0,
    val subDirs: MutableList<Dir> = mutableListOf()
) {
    fun totalSize(): Int {
        if (subDirs.isNotEmpty()) {
            return fileSize + subDirs.map { it.totalSize() }.sum()
        } else {
            return fileSize
        }
    }
}

val root = Dir(name = "/", fileSize = 0, subDirs = mutableListOf())
val dirs = mutableMapOf<String, Dir>()
val path = mutableListOf<String>()
lateinit var pwd: Dir

File("input.txt").forEachLine {
    val line = it.split(" ")

    if (line[0] == "\$" && line[1] == "cd") {
        if (line[2] == "/") {
            path.clear()
            path.add(".")
            pwd = root
        } else if (line[2] == "..") {
            path.removeLast()
        } else {
            path.add(line[2])
            pwd = dirs.getOrPut(path.joinToString(separator = "/")) {
                Dir(line[2])
            }
        }
    } else if (line[0] == "dir") {
        val d = dirs.getOrPut(path.joinToString(separator = "/") + "/" + line[1]) { Dir(line[1]) }
        pwd.subDirs.add(d)
    }

    val size = line[0].toIntOrNull()
    if (size !== null) {
        pwd.fileSize = pwd.fileSize + size
    }
}

val t = dirs.map { it.value.totalSize() }
    .filter { it <= 100_000 }.sum()
println(t)

val maxSpace = 70_000_000
val neededSpace = 30_000_000
val usedSpace = root.totalSize()
val totalUnused = maxSpace - usedSpace
val neededToDelete = neededSpace - totalUnused

val dirToDelete = dirs.map { it.value.totalSize() }.filter { it > neededToDelete }.min()
println(dirToDelete)
