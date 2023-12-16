package com.example.jigsawpuzzle

import com.example.jigsawpuzzle.algorithms.bubbleSort
import com.example.jigsawpuzzle.algorithms.insertionSort
import com.example.jigsawpuzzle.algorithms.selectionSort
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertArrayEquals
import org.junit.Test
import kotlin.random.Random

class AlgorithmsKtTest {


    @Test
    fun `given a 2d array shuffled, when sort by bubble sort, then it should be sorted`() =
        runBlocking {

            val input = arrayOf(5, 4, 2, 3, 1)
            val expected = arrayOf(1, 2, 3, 4, 5)

            input.bubbleSort(100)

            assertArrayEquals(expected, input)

        }

    @Test
    fun `given a 2d array shuffled, when sort by selection sort, then it should be sorted`() =
        runBlocking {

            val input = arrayOf(5, 4, 2, 3, 1)
            val expected = arrayOf(1, 2, 3, 4, 5)

            input.selectionSort()

            assertArrayEquals(expected, input)

        }

    @Test
    fun `given a 2d array shuffled, when sort by insertion sort, then it should be sorted`() =
        runBlocking {

            val input    = arrayOf(2, 10, 11, 16, 9, 4, 3, 8, 5, 12, 0, 1, 6, 13, 7, 14, 15, 16)
            val expected = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 16)

            input.insertionSort()

            println("expected:\t${expected.toList()}")
            println("my:\t\t\t${input.toList()}")
            assertArrayEquals(expected, input)

        }

}