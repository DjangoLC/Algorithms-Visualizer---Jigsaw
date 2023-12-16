package com.example.jigsawpuzzle

import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object BitmapCropper {

    private val scope = CoroutineScope(Dispatchers.IO)

    suspend fun splitImageIntoPieces(image: Bitmap, rows: Int, cols: Int): List<Bitmap> {
        return withContext(scope.coroutineContext) {
            val height = image.height
            val width = image.width

            val pieceWidth = width / cols
            val pieceHeight = height / rows

            val pieces = mutableListOf<Bitmap>()

            for (row in 0 until rows) {
                for (col in 0 until cols) {
                    val x = col * pieceWidth
                    val y = row * pieceHeight
                    pieces.add(Bitmap.createBitmap(image, x, y, pieceWidth, pieceHeight))
                }
            }
            pieces
        }
    }
}