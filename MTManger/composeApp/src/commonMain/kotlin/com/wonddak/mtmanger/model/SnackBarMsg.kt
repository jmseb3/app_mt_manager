package com.wonddak.mtmanger.model

data class SnackBarMsg(
    val msg: String,
    val label: String,
    val action: () -> Unit
) {
    constructor(msg:String) :this(msg,"",{})
}