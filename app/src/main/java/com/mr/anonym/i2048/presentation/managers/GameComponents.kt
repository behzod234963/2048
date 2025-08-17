package com.mr.anonym.i2048.presentation.managers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.random.Random

class GameComponents(
) {

    enum class Direction { LEFT, RIGHT, UP, DOWN }
    private val random = Random(System.currentTimeMillis())

    fun newBoard(size: Int = 4): Array<IntArray> {
        val board = Array(size) { IntArray(size) }
        addRandomTile(board)
        addRandomTile(board)
        return board
    }
    fun addRandomTile(board: Array<IntArray>): Array<IntArray> {
        val empty = mutableListOf<Pair<Int, Int>>()
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j] == 0) empty.add(i to j)
            }
        }
        if (empty.isNotEmpty()) {
            val (x, y) = empty.random(random)
            board[x][y] = if (random.nextInt(10) == 0) 4 else 2
        }
        return board
    }
    fun move(board: Array<IntArray>, direction: Direction): Pair<Array<IntArray>, Int> {
        var gained = 0
        val size = 4
        val out = Array(size) { IntArray(size) }

        fun compressAndMerge(line: IntArray): Pair<IntArray, Int> {
            val newLine = line.filter { it != 0 }.toMutableList()
            var points = 0
            var i = 0
            while (i < newLine.size - 1) {
                if (newLine[i] == newLine[i + 1]) {
                    newLine[i] *= 2
                    points += newLine[i]
                    newLine.removeAt(i + 1)
                }
                i++
            }
            while (newLine.size < size) newLine.add(0)
            return newLine.toIntArray() to points
        }

        when (direction) {
            Direction.LEFT -> {
                for (i in 0 until size) {
                    val (line, pts) = compressAndMerge(board[i])
                    out[i] = line
                    gained += pts
                }
            }
            Direction.RIGHT -> {
                for (i in 0 until size) {
                    val (line, pts) = compressAndMerge(board[i].reversedArray())
                    out[i] = line.reversedArray()
                    gained += pts
                }
            }
            Direction.UP -> {
                for (j in 0 until size) {
                    val col = IntArray(size) { board[it][j] }
                    val (line, pts) = compressAndMerge(col)
                    for (i in 0 until size) out[i][j] = line[i]
                    gained += pts
                }
            }
            Direction.DOWN -> {
                for (j in 0 until size) {
                    val col = IntArray(size) { board[it][j] }.reversedArray()
                    val (line, pts) = compressAndMerge(col)
                    val rev = line.reversedArray()
                    for (i in 0 until size) out[i][j] = rev[i]
                    gained += pts
                }
            }
        }
        return if (boardsEqual(board, out)) board to 0 else out to gained
    }
    fun getTileColor(value: Int): Color{
        return when (value) {
            0 -> Color(0xFFCDC1B4)
            2 -> Color(0xFFEEE4DA)
            4 -> Color(0xFFEDE0C8)
            8 -> Color(0xFFF2B179)
            16 -> Color(0xFFF59563)
            32 -> Color(0xFFF67C5F)
            64 -> Color(0xFFF65E3B)
            128 -> Color(0xFFEDCF72)
            256 -> Color(0xFFEDCC61)
            512 -> Color(0xFFEDC850)
            1024 -> Color(0xFFEDC53F)
            2048 -> Color(0xFFEDC22E)
            else -> Color.Black
        }
    }
    fun detectDirection(offset: Offset,thresholdPx: Float): Direction?{
        val dx = offset.x
        val dy = offset.y
        return when{
            abs(dx) > abs(dy) && dx > thresholdPx -> Direction.RIGHT
            abs(dx) > abs(dy) && dx < -thresholdPx -> Direction.LEFT
            abs(dy) > abs(dx) && dy > thresholdPx -> Direction.DOWN
            abs(dy) > abs(dx) && dy < -thresholdPx -> Direction.UP
            else -> null
        }
    }
    fun boardsEqual(b1: Array<IntArray>, b2: Array<IntArray>): Boolean {
        for (i in b1.indices) {
            if (!b1[i].contentEquals(b2[i])) return false
        }
        return true
    }
}