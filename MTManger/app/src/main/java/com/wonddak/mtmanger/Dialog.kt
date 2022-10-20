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

    fun datePicker(editText: EditText, context: Context, data: String?) {

        val calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)

        var listner = DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->
            val temp_data = "${i}.${i2 + 1}.${i3}"
            val transFormat = SimpleDateFormat("yyyy.MM.dd")
            if (data == null) {
                editText.setText(temp_data)
            } else if (data == "plan") {
                val prefs: SharedPreferences = context.getSharedPreferences("mainMT", 0)
                val mainmtid: Int = prefs.getInt("id", 0)
                GlobalScope.launch(Dispatchers.IO) {
                    var nowdata = db.MtDataDao().getMtDataById(mainmtid)
                    var mtenddata = transFormat.parse(nowdata.mtEnd)
                    var mtstartdata = transFormat.parse(nowdata.mtStart)
                    var nowcheckdata = transFormat.parse(temp_data)
                    GlobalScope.launch(Dispatchers.Main) {
                        if (nowcheckdata in mtstartdata..mtenddata) {
                            editText.setText(temp_data)
                        } else {
                            Toast.makeText(
                                context,
                                "MT 기간이 아닙니다.다시선택해주세요\n" + "MT 기간: " + nowdata.mtStart + "~" + nowdata.mtEnd,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                }.isCompleted

            } else {
                val start_date = transFormat.parse(data)
                val end_date = transFormat.parse(temp_data)
                if (start_date <= end_date) {
                    editText.setText(temp_data)
                } else {
                    Toast.makeText(context, "종료일을 다시 설정해주세요", Toast.LENGTH_SHORT).show()
                }


            }
        }

        var picker = DatePickerDialog(
            context, listner, year, month, day
        )
        picker.show()
    }

    fun planDialog(iddata: Int, postion: Int) {
        val context: Context = this.dialog.context
        val binding = DialogPlanBinding.inflate(LayoutInflater.from(context))

        dialog.setContentView(binding.root)

        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams

        GlobalScope.launch(Dispatchers.IO) {
            var nowdata = db.MtDataDao().getPlanById(postion)
            GlobalScope.launch(Dispatchers.Main) {
                if (nowdata.nowplantitle == "제목을 입력해주세요" && nowdata.nowday == "날짜를 선택해주세요" && nowdata.simpletext == "계획을 입력해주세요") {

                } else {
                    binding.dialogDate.setText(nowdata.nowday)
                    binding.dialogPlanDo.setText(nowdata.simpletext)
                    binding.dialogPlanTitle.setText(nowdata.nowplantitle)
                }

            }
        }.isCompleted

        dialog.show()

        binding.cancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.dialogDateBtn.setOnClickListener {
            datePicker(binding.dialogDate, context, "plan")
        }

        binding.ok.setOnClickListener {
            if (binding.dialogPlanTitle.text.isEmpty() or binding.dialogDate.text.isEmpty() or binding.dialogPlanTitle.text.isBlank() or binding.dialogDate.text.isBlank()) {
                Toast.makeText(context, "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    db.MtDataDao().updatePlandialogbyid(
                        postion,
                        binding.dialogDate.text.toString(),
                        binding.dialogPlanTitle.text.toString(),
                        binding.dialogPlanDo.text.toString()
                    )
                }.isCompleted
                dialog.dismiss()
            }
        }

    }

    fun personDialog(iddata: Int, postion: Int, type: Int, name: String?, number: String?) {
        //        type
//        1 ->Add
//        2 ->edit
        val context: Context = this.dialog.context
        val binding = DialogPersonBinding.inflate(LayoutInflater.from(context))

        dialog.setContentView(binding.root)

        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams

        val addperson = binding.personok
        val cancleperson = binding.personcancel
        val addfromcontact = binding.addpersonFromphonedata

        val dialogPersonName = binding.addpersonName
        val dialogPersonFee = binding.addmpersonFee
        val dialogPersonNumber = binding.addmpersonNumber

        if (name != null && number != null) {
            dialogPersonName.setText(name.toString())
            dialogPersonNumber.setText(number.toString())
        }


        if (type == 2) {
            addfromcontact.visibility = View.GONE
            binding.mtdialogtitle.text = "정보 수정"
            addperson.text = "수정"

            GlobalScope.launch(Dispatchers.IO) {
                var nowmtdata = db.MtDataDao().getPersonById(postion)
                GlobalScope.launch(Dispatchers.Main) {
                    dialogPersonName.setText(nowmtdata.name)
                    dialogPersonNumber.setText(nowmtdata.phoneNumber)
                    dialogPersonFee.setText(nowmtdata.paymentFee.toString())
                }
            }.isCompleted
        }

        dialog.show()

        cancleperson.setOnClickListener {
            dialog.dismiss()
        }

        addfromcontact.setOnClickListener {
            val permissionchecked =
                ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS)
            if (permissionchecked != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    1
                )
            } else {
                val intent = Intent(Intent.ACTION_PICK)
                intent.data = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                dialog.dismiss()
                activity.startActivityForResult(intent, 1010)
            }


        }


        addperson.setOnClickListener {

            val PersonName_temp = dialogPersonName.text.toString()
            val PersonFee_temp = dialogPersonFee.text.toString()
            val PersonNumber_temp = dialogPersonNumber.text.toString()

            if (PersonName_temp.isEmpty() or PersonFee_temp.isEmpty() or PersonNumber_temp.isEmpty() or PersonName_temp.isBlank() or PersonFee_temp.isBlank() or PersonNumber_temp.isBlank()) {
                Toast.makeText(context, "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                if (type == 1) {
                    GlobalScope.launch(Dispatchers.IO) {

                        db.MtDataDao()
                            .insertPerson(
                                Person(
                                    null,
                                    iddata,
                                    PersonName_temp,
                                    PersonNumber_temp,
                                    PersonFee_temp.toInt()
                                )
                            )
                    }.isCompleted

                    Toast.makeText(
                        context,
                        PersonName_temp + "님을 추가했습니다.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (type == 2) {
                    GlobalScope.launch(Dispatchers.IO) {
                        db.MtDataDao()
                            .insertPerson(
                                Person(
                                    postion,
                                    iddata,
                                    PersonName_temp,
                                    PersonNumber_temp,
                                    PersonFee_temp.toInt()
                                )
                            )
                    }.isCompleted


                    Toast.makeText(context, "정보를 수정했습니다.", Toast.LENGTH_SHORT).show()

                }

                dialog.dismiss()
            }

        }


    }

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

    fun buyDialog(iddata: Int, postion: Int, type: Int) {
//        type
//        1 -> add
//        2 -> edit

        val context: Context = this.dialog.context
        val binding = DialogBuyBinding.inflate(LayoutInflater.from(context))

        dialog.setContentView(binding.root)

        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams

        if (type == 1) {
            binding.addBuyCount.setText(0.toString())
        } else if (type == 2) {
            GlobalScope.launch(Dispatchers.IO) {
                var nowmtdata = db.MtDataDao().getBuyGoodById(postion)
                GlobalScope.launch(Dispatchers.Main) {
                    binding.addBuyPrice.setText(nowmtdata.price.toString())
                    binding.addBuyCount.setText(nowmtdata.count.toString())
                    binding.addBuyName.setText(nowmtdata.name)
                }
            }.isCompleted
        }
        dialog.show()


        binding.addBuyCountPlus.setOnClickListener {
            val countTemp = binding.addBuyCount.text
            binding.addBuyCount.setText((countTemp.toString().toInt() + 1).toString())
        }
        binding.addBuyCountMinus.setOnClickListener {
            val countTemp = binding.addBuyCount.text
            if (countTemp.toString() == 0.toString()) {
                binding.addBuyCount.setText(0.toString())
            } else {
                binding.addBuyCount.setText((countTemp.toString().toInt() - 1).toString())
            }
        }

        binding.cancel.setOnClickListener {
            dialog.dismiss()
        }

        GlobalScope.launch(Dispatchers.IO) {
            var nowdata = db.MtDataDao().getCategorydata()
            var items: MutableList<String> = ArrayList()
            for (element in nowdata) {
                items.add(element.name)
            }
            val myAdapter = ArrayAdapter(context, R.layout.spinner_custom, items)
            binding.addBuySpiner.adapter = myAdapter
        }

        binding.addBuySpiner.onItemSelectedListener

        binding.ok.setOnClickListener {
            val category_temp = binding.addBuySpiner.selectedItem.toString()
            val name_temp = binding.addBuyName.text.toString()
            val count_temp = binding.addBuyCount.text.toString()
            val price_temp = binding.addBuyPrice.text.toString()

            if (name_temp.isEmpty() or count_temp.isEmpty() or price_temp.isEmpty() or name_temp.isBlank() or count_temp.isBlank() or price_temp.isBlank()) {
                Toast.makeText(context, "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                var idtemp: Int? = null
                if (type == 2) {
                    idtemp = postion
                }

                GlobalScope.launch(Dispatchers.IO) {

                    db.MtDataDao()
                        .insertBuyGood(
                            BuyGood(
                                idtemp,
                                iddata,
                                category_temp,
                                name_temp,
                                count_temp.toInt(),
                                price_temp.toInt()

                            )
                        )
                }.isCompleted

            }
            if (type == 1) {

                Toast.makeText(context, name_temp + "의 구매정보를 입력했습니다.", Toast.LENGTH_SHORT)
                    .show()
            } else if (type == 2) {
                Toast.makeText(context, "구매정보를 수정했습니다.", Toast.LENGTH_SHORT).show()
            }
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


