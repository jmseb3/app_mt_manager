package com.wonddak.mtmanger.ui.view.sheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DefaultText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionSheet(
    onDismissRequest: () -> Unit = {},
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = match1,
        windowInsets = WindowInsets(0.dp,0.dp,0.dp,0.dp)
    ) {
        Column(
            modifier = Modifier.padding(top = 0.dp, bottom = bottomPadding, start = 10.dp, end = 10.dp)
        ) {
            TextButton({
                onEdit()
                onDismissRequest()
            }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Image(Icons.Default.Edit, null)
                    DefaultText(text = "수정")
                }
            }
            HorizontalDivider(
                color = match2
            )
            TextButton({
                onDelete()
                onDismissRequest()
            }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Image(Icons.Default.Delete, null)
                    DefaultText(text = "삭제")
                }
            }
        }
    }
}