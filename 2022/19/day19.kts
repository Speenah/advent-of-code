@file:OptIn(ExperimentalTime::class)

import Day19.ResourceType.CLAY
import Day19.ResourceType.GEODE
import Day19.ResourceType.OBSIDIAN
import Day19.ResourceType.ORE
import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

val blueprintCosts = File("input.txt")
    .readLines()
    .mapIndexed { index, line ->
        val ints = line.split(" ").mapNotNull { it.toIntOrNull() }
        BlueprintCost(
            id = index + 1,
            oreBotOreCost = ints[0],
            clayBotOreCost = ints[1],
            obsBotOreCost = ints[2],
            obsBotClayCost = ints[3],
            geodeBotOreCost = ints[4],
            geodeBotObsCost = ints[5]
        )
    }

var geodeBest = 0

measureTime {
    blueprintCosts.sumOf { blueprint ->
        geodeBest = 0
        blueprint.id * InventoryState(blueprint, maxTime = 24).mostGeodes()
    }.also { println(it) }
}.also { println(it) }

measureTime {
    blueprintCosts.take(3).map { blueprint ->
        geodeBest = 0
        InventoryState(blueprint, maxTime = 32).mostGeodes()
    }
        .reduce { acc, i -> acc * i }
        .also { println(it) }
}.also { println(it) }

fun InventoryState.mostGeodes(): Int {
    if (outOfTime) {
        geodeBest = geodeBest.coerceAtLeast(geodes)
        return geodes
    }

    if (cannotDoBetterThan(geodeBest)) {
        return 0
    }

    if (canBuildBot(GEODE)) {
        return collectResources()
            .buildBot(GEODE)
            .mostGeodes()
    }

    // if we have enough clay robots to always make an obsidian bot, always make an obsidian bot
    if (clayRobots >= blueprint.obsBotClayCost && canBuildBot(OBSIDIAN)) {
        return collectResources()
            .buildBot(OBSIDIAN)
            .mostGeodes()
    }

    // champion other bot cases
    var max = 0

    if (canBuildBot(OBSIDIAN)) {
        max = collectResources()
            .buildBot(OBSIDIAN)
            .mostGeodes()
            .coerceAtLeast(max)
    }

    if (canBuildBot(CLAY)) {
        max = collectResources()
            .buildBot(CLAY)
            .mostGeodes()
            .coerceAtLeast(max)
    }

    if (canBuildBot(ORE)) {
        max = collectResources()
            .buildBot(ORE)
            .mostGeodes()
            .coerceAtLeast(max)
    }

    if (lowOnOre) {
        max = collectResources()
            .mostGeodes()
            .coerceAtLeast(max)
    }

    return max
}

data class BlueprintCost(
    val id: Int,
    val oreBotOreCost: Int,
    val clayBotOreCost: Int,
    val obsBotOreCost: Int,
    val obsBotClayCost: Int,
    val geodeBotOreCost: Int,
    val geodeBotObsCost: Int
)

data class InventoryState(
    val blueprint: BlueprintCost,
    val ore: Int = 0,
    val clay: Int = 0,
    val obsidian: Int = 0,
    val geodes: Int = 0,
    val oreRobots: Int = 1,
    val clayRobots: Int = 0,
    val obsidianRobots: Int = 0,
    val geodeRobots: Int = 0,
    val time: Int = 0,
    val maxTime: Int
) {
    val outOfTime: Boolean
        get() = time >= maxTime

    val lowOnOre: Boolean
        get() = ore <= 4

    fun cannotDoBetterThan(nGeodes: Int): Boolean =
        (geodes + (0 until maxTime - time).sumOf { geodeRobots + it }) < nGeodes

    fun canBuildBot(type: ResourceType) = when (type) {
        GEODE -> ore >= blueprint.geodeBotOreCost &&
            obsidian >= blueprint.geodeBotObsCost

        OBSIDIAN -> obsidianRobots < blueprint.geodeBotObsCost &&
            ore >= blueprint.obsBotOreCost &&
            clay >= blueprint.obsBotClayCost

        CLAY -> clayRobots < blueprint.obsBotClayCost &&
            ore >= blueprint.clayBotOreCost

        ORE -> oreRobots < 4 && ore >= blueprint.oreBotOreCost
    }

    fun collectResources() = copy(
        ore = ore + oreRobots,
        clay = clay + clayRobots,
        obsidian = obsidian + obsidianRobots,
        geodes = geodes + geodeRobots,
        time = time + 1
    )

    fun buildBot(type: ResourceType) = when (type) {
        GEODE -> copy(
            ore = ore - blueprint.geodeBotOreCost,
            obsidian = obsidian - blueprint.geodeBotObsCost,
            geodeRobots = geodeRobots + 1
        )

        OBSIDIAN -> copy(
            ore = ore - blueprint.obsBotOreCost,
            clay = clay - blueprint.obsBotClayCost,
            obsidianRobots = obsidianRobots + 1
        )

        CLAY -> copy(
            ore = ore - blueprint.clayBotOreCost,
            clayRobots = clayRobots + 1
        )

        ORE -> copy(
            ore = ore - blueprint.oreBotOreCost,
            oreRobots = oreRobots + 1
        )
    }
}

enum class ResourceType {
    ORE, CLAY, OBSIDIAN, GEODE
}
