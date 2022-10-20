package com.wonddak.mtmanger.ui.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wonddak.mtmanger.AddDialog
import com.wonddak.mtmanger.databinding.ItemBuylistBinding
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.room.BuyGood
import com.wonddak.mtmanger.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class BuyRecyclerAdaptar(
        val context: Context,
        val itemlist: List<BuyGood>,
        val db: AppDatabase,
        val editor: SharedPreferences.Editor,
        val activity: MainActivity
) : RecyclerView.Adapter<BuyRecyclerAdaptar.ViewHolder>() {

    inner class ViewHolder(binding: ItemBuylistBinding) : RecyclerView.ViewHolder(binding.root) {
        val buyItemName = binding.itemBuyname
        val buyItemCount = binding.itemBuycount
        val buyItemPrice = binding.itemBuyprice
        val buyItemTotal = binding.itemBuytotal
        val buyItemCategory = binding.itemBuycategory

        val prefs: SharedPreferences = context.getSharedPreferences("mainMT", 0)
        val mainmtid: Int = prefs.getInt("id", 0)

        init {
            binding.itemBuyall.setOnLongClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    var nowdata = db.MtDataDao().getBuyGooddata()[layoutPosition].buyGoodId
                    GlobalScope.launch(Dispatchers.Main) {
                        AddDialog(context, db, editor, activity).DeleteData(mainmtid, nowdata!!, 3)
                    }
                }.isCompleted

                return@setOnLongClickListener true
            }

            binding.itemBuyall.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    var nowdata = db.MtDataDao().getBuyGooddata()[layoutPosition].buyGoodId
                    GlobalScope.launch(Dispatchers.Main) {
                        AddDialog(context, db, editor, activity).buyDialog(mainmtid,nowdata!!,2)
                    }
                }.isCompleted

            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBuylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dec = DecimalFormat("#,###")
        holder.buyItemCategory.text = itemlist[position].category
        holder.buyItemCount.text = itemlist[position].count.toString()
        holder.buyItemPrice.text = dec.format(itemlist[position].price).toString()
        holder.buyItemTotal.text = dec.format(itemlist[position].count * itemlist[position].price).toString()
        holder.buyItemName.text = itemlist[position].name
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }
}