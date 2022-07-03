package com.wonddak.mtmanger.ui.plan

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wonddak.mtmanger.AddDialog
import com.wonddak.mtmanger.core.Const
import com.wonddak.mtmanger.databinding.ItemPlanBinding
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.room.Plan
import com.wonddak.mtmanger.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class PlanRecyclerAdaptar(
    val planlist: List<Plan>,
    val context: Context,
    val db: AppDatabase,
    val editor: SharedPreferences.Editor,
    val activity: MainActivity
) : RecyclerView.Adapter<PlanRecyclerAdaptar.ViewHolder>() {

    inner class ViewHolder(binding: ItemPlanBinding) : RecyclerView.ViewHolder(binding.root) {
        val item_Title = binding.itemPlanTitle
        val item_Date = binding.itemPlanDate
        val item_do = binding.itemPlanDo
        val item_img = binding.itemPlanImg
        val add_photo_btn = binding.itemAddPhoto
        val item_img_area = binding.itemImgArea

        val prefs: SharedPreferences = context.getSharedPreferences("mainMT", 0)
        val editor = prefs.edit()
        val mainmtid: Int = prefs.getInt("id", 0)

        init {

            add_photo_btn.setOnClickListener {
                val permissionchecked1 = ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                val permissionchecked2 = ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                if (permissionchecked1 == PackageManager.PERMISSION_GRANTED && permissionchecked2 == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(Intent.ACTION_PICK)
                    GlobalScope.launch(Dispatchers.IO) {
                        var nowdata = db.MtDataDao().getPlandata(mainmtid)[layoutPosition].planId
                        editor.putInt("key", nowdata!!)
                        editor.commit()
                    }.isCompleted
                    intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*"
                    )
                    activity.startActivityForResult(intent, Const.request.imgSelect)
                } else {
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),2)
                }
            }

            item_img.setOnLongClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    var nowdata = db.MtDataDao().getPlandata(mainmtid)[layoutPosition].planId
                    GlobalScope.launch(Dispatchers.Main) {
                        AddDialog(context, db, editor, activity).DeleteData(mainmtid, nowdata!!, 7)
                    }
                }
                return@setOnLongClickListener true
            }


            binding.itemPlanCardAll.setOnLongClickListener {
                deletedata(layoutPosition, mainmtid)
                return@setOnLongClickListener true
            }
            binding.itemPlanDo.setOnLongClickListener {
                deletedata(layoutPosition, mainmtid)
                return@setOnLongClickListener true
            }
            binding.itemPlanTitle.setOnLongClickListener {
                deletedata(layoutPosition, mainmtid)
                return@setOnLongClickListener true
            }
            binding.itemPlanDate.setOnLongClickListener {
                deletedata(layoutPosition, mainmtid)
                return@setOnLongClickListener true
            }

            binding.itemPlanCardAll.setOnClickListener {
                addplandata(layoutPosition, mainmtid)
            }
            binding.itemPlanDate.setOnClickListener {
                addplandata(layoutPosition, mainmtid)
            }
            binding.itemPlanTitle.setOnClickListener {
                addplandata(layoutPosition, mainmtid)
            }
            binding.itemPlanDo.setOnClickListener {
                addplandata(layoutPosition, mainmtid)
            }

        }

    }

    fun deletedata(adapterPosition: Int, mainmtid: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            var nowdata = db.MtDataDao().getPlandata(mainmtid)[adapterPosition].planId
            GlobalScope.launch(Dispatchers.Main) {
                AddDialog(context, db, editor, activity).DeleteData(mainmtid, nowdata!!, 6)
            }
        }.isCompleted

    }

    fun addplandata(adapterposition: Int, mainmtid: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            var nowdata = db.MtDataDao().getPlandata(mainmtid)[adapterposition].planId
            GlobalScope.launch(Dispatchers.Main) {
                AddDialog(context, db, editor, activity).planDialog(mainmtid, nowdata!!)
            }
        }.isCompleted
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item_Title.text = planlist[position].nowplantitle
        holder.item_Date.text = planlist[position].nowday
        holder.item_do.text = planlist[position].simpletext
        if (planlist[position].imgsrc != "") {
            holder.item_img_area.visibility = View.VISIBLE
            holder.add_photo_btn.text = "사진수정"

            Glide.with(context)
                .load(planlist[position].imgsrc)
                .into(holder.item_img)

        } else {
            holder.item_img_area.visibility = View.GONE
        }


    }

    override fun getItemCount(): Int {
        return planlist.size

    }


}

