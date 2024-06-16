package com.wonddak.mtmanger.ui.view.home.person

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wonddak.mtmanger.room.entity.SimplePerson

@Composable
internal actual fun ContactButton(
    modifier: Modifier,
    updateValue: (SimplePerson) -> Unit,
    buttonContent: @Composable () -> Unit
) {
}