package com.wonddak.mtmanger

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.wonddak.mtmanger.databinding.ItemMtlistBinding
import com.wonddak.mtmanger.room.MtData


class MtListRecyclerAdaptar(
    val context: Context,
    val itemlist: List<MtData>,
    val editor: SharedPreferences.Editor,
    val activity : MainActivity
):RecyclerView.Adapter<MtListRecyclerAdaptar.ViewHolder>() {

    inner class ViewHolder(binding: ItemMtlistBinding) :RecyclerView.ViewHolder(binding.root){
        val itemMtTitle : TextView = binding.itemMtlisttitle
        val itemMtStart : TextView = binding.itemMtliststart
        val itemMtEnd : TextView = binding.itemMtlistend
        private val itemMtAll :LinearLayout = binding.itemMtall

        init {
            itemMtAll.setOnClickListener {
                Toast.makeText(context,""+itemlist[layoutPosition].mtDataId!!+"번째 MT로 변경했어요",Toast.LENGTH_SHORT).show()
                editor.putInt("id", itemlist[layoutPosition].mtDataId!!)
                editor.commit()

                activity.finish()
                activity.startActivity(activity.intent)
                activity.overridePendingTransition(0,0)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMtlistBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemMtTitle.text = itemlist[position].mtTitle
        holder.itemMtStart.text = itemlist[position].mtStart
        holder.itemMtEnd.text = itemlist[position].mtEnd
    }

    override fun getItemCount(): Int {
        return  itemlist.size
    }
}