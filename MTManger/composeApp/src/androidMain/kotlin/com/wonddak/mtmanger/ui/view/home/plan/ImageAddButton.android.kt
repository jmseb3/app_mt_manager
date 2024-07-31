package com.wonddak.mtmanger.ui.view.home.plan

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.wonddak.mtmanger.room.entity.Plan
import com.wonddak.mtmanger.viewModel.MTViewModel
import org.koin.compose.koinInject
import java.io.ByteArrayOutputStream

@Composable
internal actual fun ImageAddButton(modifier: Modifier, plan: Plan) {
    val mtViewModel: MTViewModel = koinInject()
    val context = LocalContext.current
    val picker =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
//                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
//                context.contentResolver.takePersistableUriPermission(uri, flag)
                mtViewModel.updatePlanImgSrc(
                    plan.planId!!,
                    uri.toString()
                )


                context.contentResolver.openInputStream(uri)?.use { fis ->

                    val byteBuffer = ByteArrayOutputStream()
                    val bufferSize = 1024
                    val buffer = ByteArray(bufferSize)
                    var len = 0
                    while (fis.read(buffer).also { len = it } != -1) {
                        byteBuffer.write(buffer, 0, len)
                    }
                    val originalImage = byteBuffer.toByteArray()
                    val bitmapImage =
                        BitmapFactory.decodeByteArray(originalImage, 0, originalImage.size)
                    val mutableBitmapImage =
                        Bitmap.createScaledBitmap(bitmapImage, bitmapImage.width, bitmapImage.height, false)
                    val outputStream = ByteArrayOutputStream()
                    mutableBitmapImage.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
                    if (mutableBitmapImage != bitmapImage) {
                        mutableBitmapImage.recycle();
                    } // else they are the same, just recycle once

                    bitmapImage.recycle();
                    mtViewModel.updatePlanImgByte(
                        plan.planId!!,
                        outputStream.toByteArray()
                    )

                }
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    IconButton(
        modifier = modifier,
        onClick = {
            picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        },
    ) {
        ImageAddButtonIcon()
    }
}