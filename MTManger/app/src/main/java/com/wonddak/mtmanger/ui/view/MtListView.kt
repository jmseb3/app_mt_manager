package com.wonddak.mtmanger.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.noRippleClickable
import com.wonddak.mtmanger.room.MtData
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match2

@Composable
fun MtListView(
    mtList: List<MtData>,
    clickItem: (mtId: Int) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            Modifier.weight(1f)
        ) {
            Spacer(
                modifier = Modifier
                    .height(10.dp)
                    .padding(vertical = 5.dp)
            )
            Text(
                text = "MT 리스트",
                color = match2,
                fontSize = 30.sp,
                fontFamily = maple,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            LazyColumn() {
                items(mtList) {
                    Text(
                        text = it.mtTitle,
                        modifier = Modifier.noRippleClickable() {
                            clickItem(it.mtDataId!!)
                        }
                    )
                }
            }
        }
    }
}