package com.wonddak.mtmanger.ui.view.plan

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.model.Resource
import com.wonddak.mtmanger.room.MtDataList
import com.wonddak.mtmanger.room.Plan
import com.wonddak.mtmanger.ui.dialog.DeleteDialog
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.viewModel.MTViewModel
import java.io.ByteArrayOutputStream

@Composable
fun PlanListView(
    mtViewModel : MTViewModel,
    itemClick: (item:Plan) -> Unit = {},
) {
    val context = LocalContext.current
    var focusId :Int? by remember {
        mutableStateOf(null)
    }
    val planResource :Resource<MtDataList> by mtViewModel.nowMtDataList.collectAsState(Resource.Loading)

    var showImgDelete by remember {
        mutableStateOf(false)
    }
    var showItemDelete by remember {
        mutableStateOf(false)
    }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia() ) {uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(uri, flag)
            focusId?.let {
                mtViewModel.updatePlanImgSrc(
                    it,
                    uri.toString()
                )
                focusId = null
            }
            context.contentResolver.openInputStream(uri)?.use { fis ->
                val byteBuffer = ByteArrayOutputStream()
                val bufferSize = 1024
                val buffer = ByteArray(bufferSize)
                var len = 0
                while (fis.read(buffer).also { len = it } != -1) {
                    byteBuffer.write(buffer, 0, len)
                }
                Log.i("JWH",byteBuffer.toByteArray().joinToString(" "))
            }
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    if (planResource is Resource.Success) {
        (planResource as Resource.Success<MtDataList>).data?.planList?.let { planList ->
            LazyColumn(
                modifier = Modifier.padding(10.dp)
            ){
                items(planList) { plan ->
                    PlanCardView(
                        plan = plan,
                        itemClick = {itemClick(plan)},
                        itemLongClick = {
                            focusId = plan.planId
                            showItemDelete = true
                        },
                        addPhoto = {
                            focusId = plan.planId
                            picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                        imgLongClick = {
                            focusId = plan.planId
                            showImgDelete = true
                        }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }
    if (showImgDelete) {
        DeleteDialog(
            stringResource(id = R.string.dialog_delete_image),
            onDismiss =  {
                showImgDelete = false
            },
            onDelete =  {
                mtViewModel.updatePlanImgSrc(focusId!!, "")
                showImgDelete = false
            }
        )
    }
    if (showItemDelete) {
        DeleteDialog(
            onDismiss =  {
                showItemDelete = false
            },
            onDelete =  {
                mtViewModel.deletePlanById(focusId!!)
                showItemDelete = false
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlanCardView(
    modifier: Modifier = Modifier,
    plan: Plan,
    itemClick: () -> Unit = {},
    itemLongClick: () -> Unit = {},
    addPhoto: () -> Unit = {},
    imgLongClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = itemClick,
                onLongClick = itemLongClick,
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
                onClick = addPhoto,
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = R.drawable.add_photo),
                    contentDescription = null,
                    tint = match1
                )
            }
            Column(
                Modifier.fillMaxWidth()
            ) {
                Text(
                    text = plan.nowplantitle,
                    color = match1,
                    fontSize = 23.sp,
                    fontFamily = maple
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = plan.nowday,
                    color = match1,
                    fontSize = 15.sp,
                    fontFamily = maple
                )
                Spacer(modifier = Modifier.height(5.dp))
                if (plan.imgsrc.isNotEmpty()) {
                    Image(
                        painter = rememberImagePainter(
                            data  = Uri.parse(plan.imgsrc)  // or ht
                        ),
                        contentDescription = "",
                        modifier = Modifier.combinedClickable(
                            onClick = { },
                            onLongClick = imgLongClick,
                        )
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = plan.simpletext,
                    color = match1,
                    fontFamily = maple
                )
            }
        }
    }

}


@Composable
@Preview
fun PlanCardViewPreview() {
    PlanCardView(plan = Plan(0, 0, "123", "제목", "계획", ""))
}