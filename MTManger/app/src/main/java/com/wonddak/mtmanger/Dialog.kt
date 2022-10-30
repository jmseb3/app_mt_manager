package com.wonddak.mtmanger

import android.Manifest
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.wonddak.mtmanger.databinding.*
import com.wonddak.mtmanger.room.*
import com.wonddak.mtmanger.ui.MainActivity
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddDialog(
    context: Context,
    val db: AppDatabase,
    val editor: SharedPreferences.Editor,
    val activity: MainActivity
) {
    val dialog = Dialog(context)
    val params = dialog.window!!.attributes
    fun DeleteData(iddata: Int, postion: Int, type: Int) {
//        type
//        1 -> person delete
//        2 -> person clear
//        3 -> buy delete
//        4 -> buy clear
//        5 -> category delete
//        6 -> plan delete
//        7 -> plan img delete

        val context: Context = this.dialog.context
        val binding = DialogDeleteBinding.inflate(LayoutInflater.from(context))

        dialog.setContentView(binding.root)

        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams
        if (type == 2 || type == 4) {
            binding.mtdialogtitle.text = "정말 초기화 하시겠습니까?"
        } else if (type == 7) {
            binding.mtdialogtitle.text = "사진을 삭제 하시겠습니까?"
        }

        dialog.show()



        binding.personok.setOnClickListener {
            if (type == 1) {
                GlobalScope.launch(Dispatchers.IO) {
                    db.MtDataDao().deletePersonById(postion)
                }.isCompleted
            } else if (type == 2) {
                GlobalScope.launch(Dispatchers.IO) {
                    db.MtDataDao().clearPersons(iddata)
                }.isCompleted
            } else if (type == 3) {
                GlobalScope.launch(Dispatchers.IO) {
                    db.MtDataDao().deleteBuyGoodById(postion)
                }.isCompleted
            } else if (type == 4) {
                GlobalScope.launch(Dispatchers.IO) {
                    db.MtDataDao().clearBuyGoods(iddata)
                }.isCompleted
            } else if (type == 5) {
                GlobalScope.launch(Dispatchers.IO) {
                    db.MtDataDao().deleteCategoryById(postion)
                }
            } else if (type == 6) {
                GlobalScope.launch(Dispatchers.IO) {
                    db.MtDataDao().deletePlanById(postion)
                }
            } else if (type == 7) {
                GlobalScope.launch(Dispatchers.IO) {
                    db.MtDataDao().updatePlanbyid(postion, "")
                }
            }

            dialog.dismiss()
        }
        binding.personcancel.setOnClickListener {
            dialog.dismiss()
        }

    }

    fun categoryDialog(postion: Int) {

        val context: Context = this.dialog.context
        val binding = DialogCategoryBinding.inflate(LayoutInflater.from(context))

        dialog.setContentView(binding.root)

        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams

        GlobalScope.launch(Dispatchers.IO) {
            var nowmtdata = db.MtDataDao().getCategorydatabyId(postion).name
            GlobalScope.launch(Dispatchers.Main) {
                binding.itemCategoyEdit.setText(nowmtdata)
            }
        }.isCompleted

        dialog.show()


        binding.cancel.setOnClickListener {
            dialog.dismiss()
        }



        binding.ok.setOnClickListener {
            val category_temp = binding.itemCategoyEdit.text.toString()

            if (category_temp.isEmpty() or category_temp.isEmpty()) {
                Toast.makeText(context, "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    var nowdata = db.MtDataDao().getCategorydata()[postion].id
                    db.MtDataDao()
                        .insertCategory(categoryList(nowdata, category_temp))

                }.isCompleted

            }

            Toast.makeText(context, "카테고리 항목을 수정했습니다.", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }


}


