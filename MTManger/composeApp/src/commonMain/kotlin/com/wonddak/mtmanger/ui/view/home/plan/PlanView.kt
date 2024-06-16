package com.wonddak.mtmanger.ui.view.home.plan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.wonddak.mtmanger.viewModel.MTViewModel
import mtmanger.composeapp.generated.resources.Res
import mtmanger.composeapp.generated.resources.add_photo
import mtmanger.composeapp.generated.resources.dialog_delete_image
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun PlanView(
    mtViewModel: MTViewModel = koinInject(),
) {
    var showPlanDialog by remember {
        mutableStateOf(false)
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            Modifier.weight(1f)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "계획",
                color = match2,
                fontSize = 30.sp,
                fontFamily = maple(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            HorizontalDivider(
                modifier = Modifier
                    .height(4.dp)
                    .clip(RoundedCornerShape(4.dp)),
                thickness = 4.dp,
                color = match2
            )
            PlanListView(
                mtViewModel = mtViewModel
            )
        }
        OutlinedButton(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            onClick = { showPlanDialog = true },
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
    val planResource: Resource<MtDataList> by mtViewModel.nowMtDataList.collectAsState(Resource.Loading)

    if (showPlanDialog && planResource is Resource.Success) {
        (planResource as Resource.Success<MtDataList>).data?.let {
            PlanDialog(
                startDate = it.mtdata.mtStart,
                endDate = it.mtdata.mtEnd,
                plan = null,
                onDismiss = {
                    showPlanDialog = false
                },
                onAdd = { title, day, text ->
                    mtViewModel.addPlan(title, day, text)
                    showPlanDialog = false
                }
            )
        }
    }
}

@Composable
fun PlanListView(
    modifier: Modifier = Modifier,
    mtViewModel: MTViewModel,
) {
    val planResource: Resource<MtDataList> by mtViewModel.nowMtDataList.collectAsState(Resource.Loading)

    if (planResource is Resource.Success) {
        (planResource as Resource.Success<MtDataList>).data?.let {
            it.planList.let { planList ->
                LazyColumn(
                    modifier = modifier.padding(10.dp)
                ) {
                    items(planList) { plan ->
                        PlanCardView(
                            plan = plan,
                            mtViewModel = mtViewModel,
                            startDate = it.mtdata.mtStart,
                            endDate = it.mtdata.mtEnd
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlanCardView(
    modifier: Modifier = Modifier,
    mtViewModel: MTViewModel,
    plan: Plan,
    startDate: String,
    endDate: String
) {
//    val context = LocalContext.current
//    val picker =
//        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
//            // Callback is invoked after the user selects a media item or closes the
//            // photo picker.
//            if (uri != null) {
//                Log.d("PhotoPicker", "Selected URI: $uri")
////                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
////                context.contentResolver.takePersistableUriPermission(uri, flag)
//                mtViewModel.updatePlanImgSrc(
//                    plan.planId!!,
//                    uri.toString()
//                )
//
//                context.contentResolver.openInputStream(uri)?.use { fis ->
//                    val byteBuffer = ByteArrayOutputStream()
//                    val bufferSize = 1024
//                    val buffer = ByteArray(bufferSize)
//                    var len = 0
//                    while (fis.read(buffer).also { len = it } != -1) {
//                        byteBuffer.write(buffer, 0, len)
//                    }
//                    Log.i("JWH", byteBuffer.toByteArray().joinToString(" "))
//                    val originalImage = byteBuffer.toByteArray()
//                    val bitmapImage =
//                        BitmapFactory.decodeByteArray(originalImage, 0, originalImage.size)
//                    val mutableBitmapImage =
//                        Bitmap.createScaledBitmap(bitmapImage, 800, 600, false)
//                    val outputStream = ByteArrayOutputStream()
//                    mutableBitmapImage.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
//                    if (mutableBitmapImage != bitmapImage) {
//                        mutableBitmapImage.recycle();
//                    } // else they are the same, just recycle once
//
//                    bitmapImage.recycle();
//                    mtViewModel.updatePlanImgByte(
//                        plan.planId!!,
//                        outputStream.toByteArray()
//                    )
//
//                }
//            } else {
//                Log.d("PhotoPicker", "No media selected")
//            }
//        }

    var showImgDelete by remember {
        mutableStateOf(false)
    }
    var showItemDelete by remember {
        mutableStateOf(false)
    }
    var showPlanDialog by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    showPlanDialog = true
                },
                onLongClick = {
                    showItemDelete = true
                },
            ),
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
            IconButton(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.TopEnd),
                onClick = {
//                    picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(resource = Res.drawable.add_photo),
                    contentDescription = null,
                    tint = match1
                )
            }
            Column(
                Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = plan.nowPlanTitle,
                    color = match1,
                    fontSize = 23.sp,
                    fontFamily = maple()
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = plan.nowDay,
                    color = match1,
                    fontSize = 15.sp,
                    fontFamily = maple()
                )
                Spacer(modifier = Modifier.height(5.dp))
                val imageModifier = Modifier
                    .combinedClickable(
                        onClick = { },
                        onLongClick = {
                            showImgDelete = true
                        },
                    )
                    .fillMaxWidth()
//                if (plan.imgBytes != null) {
//                    Image(
//                        bitmap = BitmapFactory.decodeByteArray(
//                            plan.imgBytes,
//                            0,
//                            plan.imgBytes.size
//                        ).asImageBitmap(),
//                        contentDescription = null,
//                        modifier = imageModifier,
//                        contentScale = ContentScale.Fit,
//                        alignment = Alignment.Center
//                    )
//                } else if (plan.imgSrc.isNotEmpty()) {
//                    Image(
//                        painter = rememberAsyncImagePainter(
//                            model = Uri.parse(plan.imgSrc)  // or ht
//                        ),
//                        contentDescription = "",
//                        modifier = imageModifier,
//                        alignment = Alignment.Center
//                    )
//                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = plan.simpleText,
                    color = match1,
                    fontFamily = maple()
                )
            }
        }
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
    if (showPlanDialog) {
        PlanDialog(
            startDate = startDate,
            endDate = endDate,
            plan = plan,
            onDismiss = {
                showPlanDialog = false
            },
            onAdd = { title, day, text ->
                mtViewModel.updatePlanById(
                    plan.planId!!,
                    day,
                    title,
                    text
                )
                showPlanDialog = false
            }
        )
    }

}