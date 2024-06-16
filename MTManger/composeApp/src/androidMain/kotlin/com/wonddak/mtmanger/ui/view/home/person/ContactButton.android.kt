package com.wonddak.mtmanger.ui.view.home.person

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.wonddak.mtmanger.model.SnackBarMsg
import com.wonddak.mtmanger.room.entity.SimplePerson
import com.wonddak.mtmanger.viewModel.MTViewModel
import org.koin.compose.koinInject

@Composable
internal actual fun ContactButton(
    modifier: Modifier,
    updateValue: (SimplePerson) -> Unit,
    buttonContent: @Composable () -> Unit
) {
    val mtViewModel: MTViewModel = koinInject()
    val context = LocalContext.current
    val contentLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.let {
                val uriString = it.dataString
                val uri = Uri.parse(uriString)
                val cursor = context.contentResolver.query(
                    uri,
                    arrayOf(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                    ), null, null, null
                )

                cursor!!.moveToFirst()
                val name = cursor.getString(0) //0은 이름을 얻어옵니다.
                val number = cursor.getString(1).replace("\\D".toRegex(), "") //1은 번호를 받아옵니다.
                updateValue(SimplePerson(name,number,""))
                cursor.close()
            }
        }

    fun getContentInfo() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        contentLauncher.launch(intent)
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                mtViewModel.snackBarMsg = SnackBarMsg(
                    "연락처 권한을 승인했습니다."
                )
                getContentInfo()
            } else {
                mtViewModel.snackBarMsg = SnackBarMsg(
                    "연락처 권한이 필요합니다."
                )
            }
        }
    OutlinedButton(
        modifier = modifier,
        onClick = {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getContentInfo()
            } else {
                permissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
            }
        }
    ) {
        buttonContent()
    }
}