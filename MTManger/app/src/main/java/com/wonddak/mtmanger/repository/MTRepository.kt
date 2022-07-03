package com.wonddak.mtmanger.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.wonddak.mtmanger.core.Const
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.room.MtData
import com.wonddak.mtmanger.room.MtDataDao
import com.wonddak.mtmanger.room.Person

class MTRepository(
    val mContext: Context

) {
    val prefs: SharedPreferences = mContext.getSharedPreferences(Const.pref.name, 0)
    val editor = prefs.edit()
    var mtDataDao : MtDataDao = AppDatabase.getInstance(mContext).MtDataDao()

    fun getMtDataById(id:Int) : MtData{
        return mtDataDao.getMtDataById(id)
    }

    fun getPerson(id:Int) : LiveData<List<Person>>{
        return  mtDataDao.getPerson(id)
    }

    fun getMtId( ):Int{
        return prefs.getInt(Const.pref.id, 0)
    }
}