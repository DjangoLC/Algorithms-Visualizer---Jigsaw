package com.example.jigsawpuzzle.algorithms

import com.example.jigsawpuzzle.Piece
import kotlinx.coroutines.delay
import okhttp3.internal.toImmutableList

suspend fun Array<Int>.bubbleSort(delay: Long) {
    for (i in this.indices) {
        for (j in 1..this.size - 1) {

            val current = this[j]
            val prev = this[j - 1]

            if (current < prev) {
                this[j - 1] = current
                this[j] = prev
                delay(delay)
            }
        }
    }
}

suspend fun MutableList<Piece>.bubbleSort(delay: Long, onStep: (List<Piece>) -> Unit) {
    for (i in this.indices) {
        for (j in 1..this.size - 1) {

            val current = this[j]
            val prev = this[j - 1]
            if (current.index < prev.index) {
                this[j - 1] = current
                this[j] = prev
                onStep(this.toImmutableList())
                delay(delay)
            }
        }
    }
}

suspend fun MutableList<Piece>.selectionSort(delay: Long, onStep: (List<Piece>) -> Unit) {
    for (i in this.indices) {
        var index = i
        for (j in i + 1..this.size - 1) {

            val current = this[j]
            val prev = this[index]

            if (current.index < prev.index) {
                //this[j - 1] = current
                index = j
            }
        }
        val founded = this[index]
        val prev = this[i]

        if (index != i) {
            this[i] = founded
            this[index] = prev
            onStep(this.toImmutableList())
            delay(delay)
        }
    }
}

suspend fun Array<Int>.selectionSort() {
    for (i in this.indices) {
        var index = i
        for (j in i + 1..this.size - 1) {

            val current = this[j]
            val prev = this[index]

            if (current < prev) {
                index = j
            }
        }
        val founded = this[index]
        val prev = this[i]

        if (index != i) {
            this[i] = founded
            this[index] = prev
        }
    }
}

suspend fun MutableList<Piece>.insertionSort(delay: Long, onStep: (List<Piece>) -> Unit) {
    for (i in 1 until this.size) {

        val current = this[i] //6

        var j = i - 1 // 3
        while (j >= 0 && this[j].index > current.index) {
            this[j + 1] = this[j]
            j -= 1
            onStep(this.toImmutableList())
            delay(delay / 2)
        }
        this[j + 1] = current
        onStep(this.toImmutableList())
        delay(delay / 2)
    }
}

// 3.6.1.6.8
suspend fun Array<Int>.insertionSort() {

    if (this.size < 2) return

    for (i in 1 until this.size) {

        val current = this[i] //6

        var j = i - 1 // 3
        while (j >= 0 && this[j] > current) {
            this[j + 1] = this[j]
            j--
        }
        if (j != i) {
            this[j + 1] = current
        }
    }
}



















