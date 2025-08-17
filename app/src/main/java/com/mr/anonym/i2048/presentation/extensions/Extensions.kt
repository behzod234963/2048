package com.mr.anonym.i2048.presentation.extensions

import kotlin.random.Random

fun Array<IntArray>.placeRandomTile() {
    val emptyCells = buildList {
        for (i in indices) for (j in this@placeRandomTile[i].indices)
            if (this@placeRandomTile[i][j] == 0) add(i to j)
    }
    if (emptyCells.isNotEmpty()) {
        val (i, j) = emptyCells.random()
        this[i][j] = if (Random.nextInt(10) < 9) 2 else 4
    }
}