package com.wonddak.mtmanger.ui.view.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wonddak.mtmanger.room.SimplePerson
import com.wonddak.mtmanger.viewModel.MTViewModel
import org.koin.compose.koinInject

@Composable
internal expect fun ContactButton(
    modifier: Modifier,
    updateValue: (SimplePerson) -> Unit,
    buttonContent: @Composable () -> Unit
)