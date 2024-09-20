package com.wonddak.mtmanger.ui.view.home.plan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DateWheelPicker(dateMap: Map<Int, Map<Int, List<Int>>>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        items(dateMap.keys.toList()) { year ->
            val monthMap = dateMap[year]!!
            // 연도 Wheel
            Column {
                Text(year.toString())
                MonthDateWheel(monthMap)
            }
        }
    }
}

@Composable
fun MonthDateWheel(dateMap: Map<Int, List<Int>>) {
    val sizeWeight = dateMap.map { it.value.size }
    val width = 50.dp
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        dateMap.keys.toList().forEachIndexed { index, month ->
            val dayList = dateMap[month]!!
            Column {
                Text(
                    text = month.toString(),
                    modifier = Modifier.width(width * sizeWeight[index]),
                    textAlign = TextAlign.Center
                )
                Row {
                    dayList.forEach {
                        Text(
                            text = it.toString(),
                            modifier = Modifier.width(width)
                        )
                    }
                }
            }
        }
    }
}