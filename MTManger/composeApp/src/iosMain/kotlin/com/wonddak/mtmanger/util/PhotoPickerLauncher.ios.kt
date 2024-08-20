package com.wonddak.mtmanger.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

actual class PhotoPickerLauncher actual constructor(
    private val onLaunch: () -> Unit
) {
    actual fun launch() {
        onLaunch()
    }
}

@Composable
actual fun rememberPhotoPickerLauncher(onResult: (ByteArray) -> Unit): PhotoPickerLauncher {
    val photoPickerLauncher: PhotoPickerLauncher
    val launcher = remember {
        PhotoPicker()
    }
    photoPickerLauncher = remember {
        PhotoPickerLauncher(
            onLaunch = {
                launcher.onLaunch(onResult)
            }
        )
    }
    return photoPickerLauncher
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
                                val bytes = withContext(Dispatchers.IO) {
                                    val image = UIImage.imageWithData(it)
                                    image?.toByteArray(1.0)
                                }
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