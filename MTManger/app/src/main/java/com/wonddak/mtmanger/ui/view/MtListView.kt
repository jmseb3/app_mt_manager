package com.wonddak.mtmanger.ui.view

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.noRippleClickable
import com.wonddak.mtmanger.room.MtData
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DefaultText
import com.wonddak.mtmanger.ui.view.dialog.DeleteDialog
import com.wonddak.mtmanger.viewModel.MTViewModel

@Composable
fun MtListView(
    mtViewModel: MTViewModel,
) {
    BackHandler() {
        mtViewModel.showMtList = false
    }
    val context = LocalContext.current
    val mtList by mtViewModel.getMtTotalLIst().collectAsState(initial = emptyList())

    Column(
        Modifier
            .fillMaxSize()
            .background(match1)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val mainId by mtViewModel.mainMtId.collectAsState(0)
        val filterList = mtList.filter { it.mtDataId != mainId }
        AnimatedVisibility(visible = filterList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, match2, RoundedCornerShape(10.dp))
                    .padding(5.dp)
            ) {
                items(filterList) { mtdata ->
                    MtListItem(
                        mtData = mtdata,
                        mtViewModel,
                    ) {
                        Toast.makeText(
                            context,
                            "${mtdata.mtTitle}로 변경했어요",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        mtViewModel.setMtId(mtdata.mtDataId!!)
                        mtViewModel.showMtList = false
                    }
                    Divider(
                        color = match2
                    )
                }
            }
        }
        AnimatedVisibility(visible = filterList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DefaultText(
                    text = "다른 MT 정보가 없어요"
                )
            }

        }
    }
}

@Composable
fun MtListItem(
    mtData: MtData,
    mtViewModel: MTViewModel,
    onClick: () -> Unit
) {
    var showDelete by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable(onClick)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DefaultText(
                text = mtData.mtTitle
            )
            DefaultText(
                text = "${mtData.mtStart} ~ ${mtData.mtEnd}"
            )
        }
        IconButton(onClick = { showDelete = true }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_delete_outline_24),
                contentDescription = null
            )
        }
    }

    if (showDelete) {
        DeleteDialog(
            onDelete = {
                mtViewModel.deleteMt(mtData.mtDataId!!)
                showDelete = false
            },
            onDismiss = {
                showDelete = false
            }
        )
    }


}