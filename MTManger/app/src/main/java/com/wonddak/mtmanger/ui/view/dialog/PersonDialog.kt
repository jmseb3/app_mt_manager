package com.wonddak.mtmanger.ui.view.dialog

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.room.Person
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.view.common.DefaultText
import com.wonddak.mtmanger.ui.view.common.DialogBase
import com.wonddak.mtmanger.ui.view.common.DialogTextField

@Composable
fun PersonDialog(
    person: Person?,
    onAdd: (
        name: String,
        number: String,
        fee: String
    ) -> Unit = { _, _, _ -> },
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val focusRequester by remember { mutableStateOf(FocusRequester()) }

    var name by remember {
        mutableStateOf(person?.name ?: "")
    }
    var number by remember {
        mutableStateOf(person?.phoneNumber ?: "")
    }
    var fee by remember {
        mutableStateOf(person?.paymentFee?.toString() ?: "")
    }
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
                name = cursor.getString(0) //0은 이름을 얻어옵니다.
                number = cursor.getString(1).replace("\\D".toRegex(), "") //1은 번호를 받아옵니다.
                focusRequester.requestFocus()
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
                Toast.makeText(context, "연락처 권한을 승인했습니다..", Toast.LENGTH_SHORT).show()
                getContentInfo()
            } else {
                Toast.makeText(
                    context, "연락처 권한이 필요합니다.", Toast.LENGTH_SHORT
                ).show()
            }
        }
    DialogBase(
        titleText = if (person != null) "인원 수정" else "인원 추가",
        confirmText = if (person != null) "수정" else "추가",
        onConfirm = {
            if (name.isEmpty() || number.isEmpty() || fee.isEmpty()) {
                Toast.makeText(
                    context,
                    context.getString(R.string.dialog_error_field),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                onAdd(name, number, fee)
            }
        }, onDismiss = onDismiss
    ) {
        Column() {
            Box() {
                OutlinedButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.READ_CONTACTS
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            getContentInfo()
                        } else {
                            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                        }
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DefaultText(
                            text = "연락처에서 불러오기",
                            color = match1
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_contact_phone_24),
                            contentDescription = null,
                            tint = match1
                        )
                    }

                }
            }
            DialogTextField(
                value = name,
                placeHolder = "이름 입력해 주세요",
                label = "이름",
                change = { name = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
            DialogTextField(
                value = number,
                placeHolder = "전화번호를 입력하세요",
                label = "전화번호",
                change = {
                    number = it.replace("\\D".toRegex(), "")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            DialogTextField(
                value = fee,
                placeHolder = "납부금액을 입력해 주세요",
                label = "납부금액",
                change = {
                    fee = it.replace("\\D".toRegex(), "")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier.focusRequester(focusRequester)
            )
        }
    }
}