package com.wonddak.mtmanger.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.wonddak.mtmanger.core.Const
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.room.MtData
import com.wonddak.mtmanger.room.MtDataDao
import com.wonddak.mtmanger.room.Person
import java.text.SimpleDateFormat
import javax.inject.Inject

class MTRepository @Inject constructor(
    val mtDataDao: MtDataDao
) {

    fun getMtDataById(id: Int): MtData {
        return mtDataDao.getMtDataById(id)
    }

    fun getPerson(id: Int): LiveData<List<Person>> {
        return mtDataDao.getPerson(id)
    }


    suspend fun checkEndDateById(mtId: Int,tempDate :String) :Boolean{
        val transFormat = SimpleDateFormat("yyyy.MM.dd")

        val nowDate = mtDataDao.getMtDataById(mtId)

        val mtEndData = transFormat.parse(nowDate.mtEnd)
        val mtStartData = transFormat.parse(nowDate.mtStart)
        val nowCheckData = transFormat.parse(tempDate)

        return (nowCheckData in mtStartData..mtEndData)
    }

    suspend fun insertMtData(mtData: MtData):Long {
        return mtDataDao.insertMtData(mtData)
    }

    fun getMtTotalList() :LiveData<List<MtData>>{
        return mtDataDao.getMtData()
    }

}