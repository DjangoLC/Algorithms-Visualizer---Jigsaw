package com.example.jigsawpuzzle

import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jigsawpuzzle.algorithms.BubbleSort
import com.example.jigsawpuzzle.algorithms.ListSorter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList

class JigsawViewModel : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _pieces = MutableSharedFlow<List<Piece>>()
    val pieces: Flow<List<Piece>> = _pieces
    private val _algorithm = MutableSharedFlow<SortAlgorithm>()
    val algorithm: Flow<SortAlgorithm> = _algorithm

    private var piecesPuzzle: MutableList<Piece> = mutableListOf()
    private var sortAlgorithm = SortAlgorithm.BUBBLE
    private var listSort: ListSorter<Piece> = BubbleSort()
    private var workJob: Job? = null
    private val images = listOf(
        R.drawable.puppy,
        R.drawable.outside,
        R.drawable.man
    )
    private var resId = images.random()

    fun changeAlgorithm() {
        sortAlgorithm = SortAlgorithm.changeAlgorithm(sortAlgorithm)
        listSort = ListSorter.getSortAlgorithm(sortAlgorithm)
        scope.launch { _algorithm.emit(sortAlgorithm) }
    }

    fun changeImage(resource: Resources) {
        resId = images.random()
        init(resource)
    }

    fun init(resource: Resources) {
        viewModelScope.launch(scope.coroutineContext) {
            val piecesImage = BitmapCropper.splitImageIntoPieces(BitmapFactory.decodeResource(resource, resId), 5, 5)

            val mappedPieces = piecesImage.mapIndexed { index, bitmap ->
                Piece(index, bitmap)
            }
            piecesPuzzle.clear()
            piecesPuzzle.addAll(mappedPieces)
            shuffle()
        }
    }

    fun shuffle() {
        workJob?.cancel()
        viewModelScope.launch {
            piecesPuzzle.shuffle()
            _pieces.emit(piecesPuzzle.toImmutableList())
        }
    }

    fun sort() {
        workJob?.cancel()
        workJob = viewModelScope.launch {
            listSort.sort(piecesPuzzle, sortAlgorithm.delay) { updates ->
                scope.launch {
                    println(updates.map { it.index })
                    _pieces.emit(updates.toImmutableList())
                }
            }
        }
    }
}

enum class SortAlgorithm(val delay: Long) {
    BUBBLE(100),
    SELECTION(100),
    INSERTION(100);

    companion object {
        fun changeAlgorithm(sortAlgorithm: SortAlgorithm) = when (sortAlgorithm) {
            BUBBLE -> SELECTION
            SELECTION -> INSERTION
            INSERTION -> BUBBLE
        }
    }

}