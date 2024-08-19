package com.wonddak.mtmanger.ui.view.home.plan

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerConfigurationSelectionOrdered
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.darwin.NSObject
import platform.posix.memcpy

@Composable
internal actual fun ImageAddButton(modifier: Modifier, color: Color, action : (ByteArray) -> Unit) {
    val launcher = remember {
        PhotoPicker()
    }

    IconButton(
        modifier = modifier,
        onClick = {
            launcher.onLaunch() {
                action(it)
            }
        },
    ) {
        ImageAddButtonIcon(color)
    }
}


class PhotoPicker() {

    private var onResult :suspend (ByteArray) -> Unit = {}

    fun onLaunch(onResult : suspend (ByteArray)  -> Unit) {
        this.onResult = onResult
        val pickerController = createPHPickerViewController(delegate)
        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            pickerController,
            true,
            null,
        )
    }


    private val delegate =
        object : NSObject(), PHPickerViewControllerDelegateProtocol {
            override fun picker(
                picker: PHPickerViewController,
                didFinishPicking: List<*>,
            ) {
                picker.dismissViewControllerAnimated(flag = true, completion = null)
                @Suppress("UNCHECKED_CAST")
                val results = didFinishPicking as List<PHPickerResult>

                for (result in results) {
                    result.itemProvider.loadDataRepresentationForTypeIdentifier(
                        typeIdentifier = "public.image",
                    ) { nsData, _ ->
                        CoroutineScope(Dispatchers.Main).launch {
                            nsData?.let {
                                val image = UIImage.imageWithData(it)
                                val bytes = image?.toByteArray(1.0)
                                if (bytes != null) {
                                    onResult(bytes)
                                }
                            }
                        }
                    }
                }
            }
        }

    private fun createPHPickerViewController(
        delegate: PHPickerViewControllerDelegateProtocol
    ): PHPickerViewController {
        val pickerViewController =
            PHPickerViewController(
                configuration =
                PHPickerConfiguration().apply {
                    setSelectionLimit(selectionLimit = 1)
                    setFilter(filter = PHPickerFilter.imagesFilter)
                    setSelection(selection = PHPickerConfigurationSelectionOrdered)
                }
            )
        pickerViewController.delegate = delegate
        return pickerViewController
    }


    @OptIn(ExperimentalForeignApi::class)
    private fun UIImage.toByteArray(compressionQuality: Double): ByteArray {
        val validCompressionQuality = compressionQuality.coerceIn(0.0, 1.0)
        val jpegData = UIImageJPEGRepresentation(this, validCompressionQuality)!!
        return ByteArray(jpegData.length.toInt()).apply {
            memcpy(this.refTo(0), jpegData.bytes, jpegData.length)
        }
    }
}