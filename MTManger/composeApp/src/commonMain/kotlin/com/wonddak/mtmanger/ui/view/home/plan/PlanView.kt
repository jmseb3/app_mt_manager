package com.wonddak.mtmanger.ui.view.home.plan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.model.Resource
import com.wonddak.mtmanger.room.entity.MtDataList
import com.wonddak.mtmanger.room.entity.Plan
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.dialog.DeleteDialog
import com.wonddak.mtmanger.ui.view.sheet.OptionSheet
import com.wonddak.mtmanger.ui.view.sheet.OptionSheetItem
import com.wonddak.mtmanger.util.rememberPhotoPickerLauncher
import com.wonddak.mtmanger.viewModel.MTViewModel
import mtmanger.composeapp.generated.resources.Res
import mtmanger.composeapp.generated.resources.add_photo
import mtmanger.composeapp.generated.resources.baseline_link_24
import mtmanger.composeapp.generated.resources.camera_switch
import mtmanger.composeapp.generated.resources.dialog_delete_image
import mtmanger.composeapp.generated.resources.no_photography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PlanView(
    mtViewModel: MTViewModel = koinViewModel(),
    navigateNew: (start: String, end: String) -> Unit,
    navigateEdit: (start: String, end: String,plan :Int) -> Unit,
) {
    val planResource: Resource<MtDataList> by mtViewModel.nowMtDataList.collectAsState(Resource.Loading)

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (planResource is Resource.Success) {
            (planResource as Resource.Success<MtDataList>).data?.let { mtDataList ->
                Column(
                    Modifier.weight(1f)
                ) {
                    mtDataList.planList.let { planList ->
                        LazyColumn(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            items(planList) { plan ->
                                PlanCardView(
                                    plan = plan,
                                    mtViewModel = mtViewModel,
                                    navigateEdit = {
                                        navigateEdit(
                                            mtDataList.mtData.mtStart,
                                            mtDataList.mtData.mtEnd,
                                            plan.planId!!
                                        )
                                    }
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                            }
                        }
                    }
                }
                OutlinedButton(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    onClick = {
                        navigateNew(
                            mtDataList.mtData.mtStart,
                            mtDataList.mtData.mtEnd
                        )
                    },
                    border = BorderStroke(2.dp, match2)
                ) {
                    Text(
                        text = "계획 추가하기",
                        color = match2,
                        fontFamily = maple(),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun PlanCardView(
    modifier: Modifier = Modifier,
    mtViewModel: MTViewModel,
    plan: Plan,
    navigateEdit: () -> Unit
) {
    var showOptionSheet by remember {
        mutableStateOf(false)
    }
    var showImgDelete by remember {
        mutableStateOf(false)
    }
    var showItemDelete by remember {
        mutableStateOf(false)
    }
    val photoPickerLauncher = rememberPhotoPickerLauncher(
        onResult = {
            mtViewModel.updatePlanImgByte(plan.planId!!, it)
        }
    )
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                showOptionSheet = true
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = match2
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 3.dp)
        ) {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(
                    text = plan.nowPlanTitle,
                    color = match1,
                    fontSize = 23.sp,
                    fontFamily = maple()
                )
                Text(
                    text = plan.nowDay,
                    color = match1,
                    fontSize = 15.sp,
                    fontFamily = maple()
                )
                plan.link.takeIf {
                    val urlRegex = """(http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?""".toRegex()
                    it.isNotEmpty()&& urlRegex.matches(it)
                }?.let {url ->
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {

                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = match1
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(painter = painterResource(Res.drawable.baseline_link_24),null)
                            Text(url)
                        }
                    }
                }
                PlanImageView(
                    Modifier
                        .fillMaxWidth(),
                    plan
                )
                Text(
                    text = plan.simpleText,
                    color = match1,
                    fontFamily = maple()
                )
            }
        }
    }
    if (showOptionSheet) {
        OptionSheet(
            onDismissRequest = { showOptionSheet = false },
            arrayListOf<OptionSheetItem<*>>(
                OptionSheetItem.OptionEdit("계획 수정") {
                    navigateEdit()
                },
                OptionSheetItem.OptionDelete("계획 삭제") {
                    showItemDelete = true
                }
            ).also {
                if (plan.imageExist) {
                    it.add(
                        OptionSheetItem.Drawable(
                            Res.drawable.camera_switch,
                            "사진 변경"
                        ) {
                            photoPickerLauncher.launch()
                        }
                    )
                    it.add(
                        OptionSheetItem.Drawable(
                            Res.drawable.no_photography,
                            "사진 삭제"
                        ) {
                            showImgDelete = true
                        }
                    )
                } else {
                    it.add(
                        OptionSheetItem.Drawable(
                            image = Res.drawable.add_photo,
                            title = "사진 추가"
                        ) {
                            photoPickerLauncher.launch()
                        }
                    )
                }
            }
        )
    }
    if (showImgDelete) {
        DeleteDialog(
            stringResource(resource = Res.string.dialog_delete_image),
            onDismiss = {
                showImgDelete = false
            },
            onDelete = {
                mtViewModel.clearPlanImgSrc(plan.planId!!)
                showImgDelete = false
            }
        )
    }
    if (showItemDelete) {
        DeleteDialog(
            onDismiss = {
                showItemDelete = false
            },
            onDelete = {
                mtViewModel.deletePlanById(plan.planId!!)
                showItemDelete = false
            }
        )
    }
}