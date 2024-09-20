package com.wonddak.mtmanger.ui.view.home.plan

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DateWheelPicker(dateMap: Map<Int, Map<Int, List<Int>>>) {
    val width = 70.dp
    val scroll = rememberScrollState()
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(scroll)
            .border(1.dp, Color.Red)
    ){
        dateMap.keys.forEach { year ->
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.Top
            ) {
                dateMap[year]!!.keys.toList().forEach { month ->
                    val dayList = dateMap[year]!![month]!!
                    Row {
                        dayList.forEach {
                            TextButton(
                                onClick = {

                                },
                                modifier = Modifier.width(width)
                            ) {
                                Text(
                                    text = "$month / $it",
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}