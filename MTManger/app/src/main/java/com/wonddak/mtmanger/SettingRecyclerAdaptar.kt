package com.wonddak.mtmanger

import android.content.Context

import android.content.SharedPreferences
import android.util.Log

import android.view.LayoutInflater
import android.view.ViewGroup

import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.wonddak.mtmanger.databinding.ItemCategoryBinding

import com.wonddak.mtmanger.room.AppDatabase

import com.wonddak.mtmanger.room.categoryList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SettingRecyclerAdaptar(
    val itemlist: List<categoryList>,
    val context: Context,
    val db: AppDatabase,
    val editor: SharedPreferences.Editor,
    val activity: MainActivity

) : RecyclerView.Adapter<SettingRecyclerAdaptar.ViewHolder>() {

    inner class ViewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemname: TextView = binding.itemCategory

        val prefs: SharedPreferences = context.getSharedPreferences("mainMT", 0)
        val mainmtid: Int = prefs.getInt("id", 0)

        init {
            itemname.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    var nowdata = db.MtDataDao().getCategorydata()[layoutPosition].id
                    Log.d("datas:",""+nowdata)
                    GlobalScope.launch(Dispatchers.Main) {
                        AddDialog(context, db, editor, activity).categoryDialog(nowdata!!)
                    }
                }.isCompleted
            }

            itemname.setOnLongClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    var nowdata = db.MtDataDao().getCategorydata()[layoutPosition].id
                    GlobalScope.launch(Dispatchers.Main) {
                        AddDialog(context, db, editor, activity).DeleteData(mainmtid,nowdata!!,5)
                    }
                }.isCompleted
                return@setOnLongClickListener true
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemname.text = itemlist[position].name
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }
}