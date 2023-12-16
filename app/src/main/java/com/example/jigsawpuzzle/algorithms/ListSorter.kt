package com.example.jigsawpuzzle.algorithms

import com.example.jigsawpuzzle.Piece
import com.example.jigsawpuzzle.SortAlgorithm

interface ListSorter<T> {
    suspend fun sort(elements: MutableList<T>, delay: Long = 0, onSorted: (List<T>) -> Unit)

    companion object {
        fun getSortAlgorithm(sortAlgorithm: SortAlgorithm) = when(sortAlgorithm) {
            SortAlgorithm.BUBBLE -> BubbleSort()
            SortAlgorithm.SELECTION -> SelectionSort()
            SortAlgorithm.INSERTION -> InsertionSort()
        }
    }
}

class BubbleSort: ListSorter<Piece> {
    override suspend fun sort(elements: MutableList<Piece>, delay: Long, onSorted: (List<Piece>) -> Unit) {
        elements.bubbleSort(delay) {
            onSorted.invoke(it)
        }
    }
}

class SelectionSort: ListSorter<Piece> {
    override suspend fun sort(elements: MutableList<Piece>, delay: Long, onSorted: (List<Piece>) -> Unit) {
        elements.selectionSort(delay) {
            onSorted.invoke(it)
        }
    }
}

class InsertionSort: ListSorter<Piece> {
    override suspend fun sort(elements: MutableList<Piece>, delay: Long, onSorted: (List<Piece>) -> Unit) {
        elements.insertionSort(delay) {
            onSorted.invoke(it)
        }
    }
}