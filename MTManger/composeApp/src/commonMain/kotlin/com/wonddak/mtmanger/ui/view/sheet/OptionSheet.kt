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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DefaultText
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

sealed class OptionSheetItem<T>(
    open val image :T,
    open val title :String,
    open val action : () -> Unit
) {
    data class OptionEdit(
        override val title : String = "수정",
        override val action: () -> Unit
    ) : OptionSheetItem<ImageVector>(
        image = Icons.Default.Edit,
        title = title,
        action = action
    )

    data class OptionDelete(
        override val title : String = "삭제",
        override val action: () -> Unit
    ) : OptionSheetItem<ImageVector>(
        image = Icons.Default.Delete,
        title = title,
        action = action
    )

    data class Drawable(
        override val image : DrawableResource,
        override val title: String,
        override val action: () -> Unit
    ): OptionSheetItem<DrawableResource>(image, title, action)
}


@Composable
fun DefaultOptionSheet(
    onDismissRequest: () -> Unit = {},
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
   OptionSheet(
       onDismissRequest,
       listOf(
           OptionSheetItem.OptionEdit(action = onEdit),
           OptionSheetItem.OptionEdit(action = onDelete),
       )
   )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionSheet(
    onDismissRequest: () -> Unit = {},
    optionItems : List<OptionSheetItem<*>>
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
            optionItems.forEachIndexed { index, optionItem ->
                TextButton({
                    optionItem.action.invoke()
                    onDismissRequest()
                }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        with(optionItem.image) {
                            if (this is ImageVector) {
                                Image(
                                    this,
                                    null,
                                    Modifier.size(24.dp)
                                )
                            } else if (this is DrawableResource) {
                                Image(
                                    painter = painterResource(this),
                                    null,
                                    Modifier.size(24.dp)
                                )
                            }
                        }
                        DefaultText(text = optionItem.title)
                    }
                }
                if (index != optionItems.size -1) {
                    HorizontalDivider(
                        color = match2
                    )
                }
            }
        }
    }
}