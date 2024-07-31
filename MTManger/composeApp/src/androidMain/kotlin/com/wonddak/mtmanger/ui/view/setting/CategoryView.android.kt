package com.wonddak.mtmanger.ui.view.setting

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wonddak.mtmanger.room.entity.categoryList

@Composable
@Preview
fun CategoryViewPreview(){
    CategoryView(
        Modifier,
        listOf(
            categoryList(null,"test1"),
            categoryList(null,"test2"),
            categoryList(null,"test3"),
            categoryList(null,"test4"),
        ))
}