import java.io.File
import kotlin.math.max

data class Valve(val name: String, val flowRate: Int, val tunnels: List<String>)

val valves = File("input.txt").readLines().map {
    val vNames = Regex("[A-Z]{2}").findAll(it)!!.toList().map { it.value }
    val rate = Regex("=(\\d{1,2});").find(it)!!.groupValues[1].toInt()
    Valve(vNames.first(), rate, vNames.drop(1))
}.associateBy { it.name }

val paths = findShortestPaths(valves)

var flowRate = 0
var minutes = 30

dfs()
println(flowRate)

flowRate = 0
minutes = 26

dfs(callTwice = true)
println(flowRate)

////////////////////////////////////
fun dfs(
    time: Int = 0,
    currFlowRate: Int = 0,
    currValve: String = "AA",
    visited: Set<String> = emptySet(),
    callTwice: Boolean = false
) {
    flowRate = max(flowRate, currFlowRate)

    paths.getValue(currValve).forEach { (valve, dist) ->
        if (!visited.contains(valve) && time + dist + 1 < minutes) {
            dfs(
                time = time + dist + 1,
                currFlowRate = currFlowRate + (minutes - time - dist - 1) * valves[valve]!!.flowRate,
                currValve = valve,
                visited = visited + setOf(valve),
                callTwice = callTwice
            )
        }
    }

    if (callTwice) {
        dfs(
            time = 0,
            currFlowRate = currFlowRate,
            currValve = "AA",
            visited = visited,
            callTwice = false
        )
    }
}

fun findShortestPaths(valves: Map<String, Valve>): Map<String, Map<String, Int>> {
    val paths = valves.values.associate {
        it.name to it.tunnels.associateWith { 1 }.toMutableMap()
    }.toMutableMap()

    // Floyd-Warshall
    for (i in paths.keys) {
        for (j in paths.keys) {
            for (k in paths.keys) {
                val ik = paths[i]?.get(k) ?: 1_000
                val kj = paths[k]?.get(j) ?: 1_000
                val ij = paths[i]?.get(j) ?: 1_000
                if (ik + kj < ij) {
                    paths[i]?.set(j, ik + kj)
                }
            }
        }
    }

    paths.values.forEach {
        it.keys.map { key -> if (valves[key]?.flowRate == 0) key else "" }
            .forEach { toRemove -> if (toRemove != "") it.remove(toRemove) }
    }

    // drop paths to a valve with rate 0
    return paths
}
