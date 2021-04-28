package com.wonddak.mtmanger

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wonddak.mtmanger.databinding.ItemPersonlistBinding
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.room.Person
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class PersonRecyclerAdaptar(
        val personlist: List<Person>,
        val context: Context,
        val db: AppDatabase,
        val editor: SharedPreferences.Editor,
        val activity: MainActivity

) :RecyclerView.Adapter<PersonRecyclerAdaptar.ViewHolder>(){

    inner class ViewHolder(binding: ItemPersonlistBinding) : RecyclerView.ViewHolder(binding.root){
        val itemPersonName : TextView = binding.itemPersonname
        val itemPersonFee : TextView = binding.itemPersonfee
        val itemPersonNumber : ImageView = binding.itemPersonphone
        val itemAll = binding.itemPersonall

        val prefs: SharedPreferences = context.getSharedPreferences("mainMT", 0)
        val mainmtid: Int = prefs.getInt("id", 0)

        init {
            itemAll.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    var nowdata = db.MtDataDao().getPersondata()[layoutPosition].personId
                    GlobalScope.launch(Dispatchers.Main) {
                        AddDialog(context, db, editor, activity).personDialog(mainmtid, nowdata!!,2,null,null)
                    }
                }.isCompleted

            }
            itemAll.setOnLongClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    var nowdata = db.MtDataDao().getPersondata()[layoutPosition].personId
                    GlobalScope.launch(Dispatchers.Main) {
                        AddDialog(context, db, editor, activity).DeleteData(mainmtid,nowdata!!,1)
                    }
                }
                return@setOnLongClickListener true
            }

            itemPersonNumber.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    var nowdata = db.MtDataDao().getPersondata()[layoutPosition].phoneNumber
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +nowdata))
                    activity.startActivity(intent)
                }

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding  = ItemPersonlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dec = DecimalFormat("#,###")
        holder.itemPersonName.text = personlist[position].name
        holder.itemPersonFee.text = dec.format(personlist[position].paymentFee).toString() +"원"
    }

    override fun getItemCount(): Int {
        return  personlist.size
    }
}