package com.example.jigsawpuzzle


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.jigsawpuzzle.ui.theme.JigsawPuzzleTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JigsawPuzzleTheme {
                // A surface container using the 'background' color from the theme

                val scope = rememberCoroutineScope()
                val viewModel: JigsawViewModel by viewModels()

                val pieces = viewModel.pieces.collectAsState(initial = emptyList())
                val algorithm = viewModel.algorithm.collectAsState(initial = SortAlgorithm.BUBBLE)

                viewModel.init(resources)

                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    },
                    modifier = Modifier.fillMaxSize()
                ) { _ ->
                    BuildScreen(
                        pieces = pieces.value,
                        algorithm = algorithm.value,
                        onSortButtonClick = {
                            viewModel.sort()
                        }, onShuffleButtonClick = {
                            viewModel.shuffle()
                        },
                        onChangeAlgorithmButtonClick = {
                            viewModel.changeAlgorithm()
                        },
                        onImageClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "index: $it",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        onChangeImageClick = {
                            viewModel.changeImage(resources)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BuildScreen(
    pieces: List<Piece>,
    algorithm: SortAlgorithm,
    onSortButtonClick: () -> Unit,
    onShuffleButtonClick: () -> Unit,
    onChangeAlgorithmButtonClick: () -> Unit,
    onImageClick: (Int) -> Unit,
    onChangeImageClick: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff151515))
    ) {
        Text(
            text = "Algorithm strategy: $algorithm",
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        JigsawGrid(
            pieces = pieces,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .height(350.dp),
            onImageClick = onImageClick
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp), onClick = {
            onSortButtonClick.invoke()
        }) {
            Text(text = "Sort")
        }
        OutlinedButton(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
            onClick = {
                onShuffleButtonClick.invoke()
            }) {
            Text(text = "Unsort")
        }
        OutlinedButton(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
            onClick = {
                onChangeAlgorithmButtonClick.invoke()
            }) {
            Text(text = "Change algorithm")
        }
        OutlinedButton(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
            onClick = {
                onChangeImageClick.invoke()
            }) {
            Text(text = "Change image")
        }
    }
}

@Composable
fun JigsawGrid(
    pieces: List<Piece>, modifier: Modifier = Modifier, onImageClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5), modifier = modifier
    ) {
        items(pieces.size) { index ->
            JigsawPiece(piece = pieces[index], onImageClick = onImageClick)
        }
    }
}

@Composable
fun JigsawPiece(piece: Piece, onImageClick: (Int) -> Unit) {
    Column(Modifier.clickable { onImageClick(piece.index) }) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(piece.bitmap)
                .dispatcher(Dispatchers.IO).diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED).crossfade(true).build(),
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun JigSawPreview() {
    BuildScreen(
        pieces = (1..8).map { Piece(it, null) },
        algorithm = SortAlgorithm.BUBBLE,
        onSortButtonClick = {},
        onShuffleButtonClick = {},
        onChangeAlgorithmButtonClick = {},
        onImageClick = {},
        onChangeImageClick = {})
}
