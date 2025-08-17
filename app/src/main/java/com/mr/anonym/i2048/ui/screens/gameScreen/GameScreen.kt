package com.mr.anonym.i2048.ui.screens.gameScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mr.anonym.i2048.R
import com.mr.anonym.i2048.presentation.managers.GameComponents

@Composable
fun GameScreen(
    navController: NavController
) {

//    Context
    val context = LocalContext.current

//    Objects
    val gameComponents = GameComponents()

//    State
    var board by remember { mutableStateOf(gameComponents.newBoard()) }
    var score = remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val thresholdPx = with(density) { 48.dp.toPx() }

//    UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFBBADA0))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${stringResource(R.string.score)}: ${score.intValue}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Button(
                onClick = {
                    board = gameComponents.newBoard()
                    score.intValue = 0
                }
            ) {
                Text("Новая игра")
            }
        }
        Spacer(Modifier.height(16.dp))
        var dragOffset by remember { mutableStateOf(Offset.Zero) }
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth()
                .background(Color(0xFF776E65), RoundedCornerShape(10.dp))
                .pointerInput(board,score) {
                    detectDragGestures (
                        onDragStart = { dragOffset = Offset.Zero },
                        onDrag = { _,dragAmount -> dragOffset += dragAmount },
                        onDragEnd = {
                            val dir = gameComponents.detectDirection( dragOffset,thresholdPx )
                            dir?.let{ direction ->
                                val ( moved,pts ) = gameComponents.move(board,direction)
                                if ( !gameComponents.boardsEqual(board,moved) ){
                                    board = gameComponents.addRandomTile(moved)
                                    score.intValue += pts
                                }
                            }
                            dragOffset = Offset.Zero
                        }
                    )
                }
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                repeat(4){ i->
                    Row (
                        modifier = Modifier.weight(1f)
                    ){
                        repeat(4){ j->
                            val v = board[i][j]
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .padding(4.dp)
                                    .background(gameComponents.getTileColor(v), RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ){
                                if (v != 0){
                                    Text(
                                        text = v.toString(),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewGameScreen() {
    GameScreen(
        navController = NavController(LocalContext.current)
    )
}