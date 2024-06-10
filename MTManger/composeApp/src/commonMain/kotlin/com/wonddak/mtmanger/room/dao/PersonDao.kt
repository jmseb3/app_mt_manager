package com.wonddak.mtmanger.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wonddak.mtmanger.room.entity.Person

@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPerson(person: Person)

    @Query("DELETE FROM Person WHERE personId = :personId")
    fun deletePersonById(personId: Int)

    @Query("UPDATE Person SET name = :name,paymentFee = :fee,phoneNumber =:number  WHERE personId = :personId")
    fun updatePersonData(name: String, fee: Int, number: String, personId: Int)

    @Query("DELETE FROM person WHERE mtId= :mtid")
    fun clearPersons(mtid: Int)
}