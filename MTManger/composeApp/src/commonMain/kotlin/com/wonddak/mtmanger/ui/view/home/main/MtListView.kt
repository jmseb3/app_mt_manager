package com.wonddak.mtmanger.ui.view.home.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonddak.mtmanger.noRippleClickable
import com.wonddak.mtmanger.room.entity.MtData
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DefaultText
import com.wonddak.mtmanger.viewModel.MTViewModel
import org.koin.compose.koinInject

@Composable
fun MtListView(
    mtViewModel: MTViewModel = koinInject(),
    close: (MtData) -> Unit
) {
    val mtList by mtViewModel.totalMtList.collectAsState()
    Column(
        Modifier
            .fillMaxSize()
            .background(match1)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            Modifier.weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, match2, RoundedCornerShape(10.dp))
                    .padding(5.dp)
            ) {
                items(mtList) { mtData ->
                    MtListItem(mtData = mtData) {
                        close(mtData)
                    }
                    HorizontalDivider(
                        color = match2
                    )
                }
            }
        }
    }
}

@Composable
fun MtListItem(
    mtData: MtData,
    onClick: () -> Unit
) {
    TextButton(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable(onClick)
            .padding(10.dp),
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DefaultText(
                text = mtData.simpleTitle
            )
        }

    }
}