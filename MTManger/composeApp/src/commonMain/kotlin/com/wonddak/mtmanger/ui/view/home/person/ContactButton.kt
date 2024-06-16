package com.wonddak.mtmanger.ui.view.home.person

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wonddak.mtmanger.room.entity.*

@Composable
internal expect fun  ContactButton(
    modifier: Modifier,
    updateValue: (SimplePerson) -> Unit,
    buttonContent: @Composable () -> Unit
)