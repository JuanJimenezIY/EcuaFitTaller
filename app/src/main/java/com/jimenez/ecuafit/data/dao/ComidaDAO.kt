package com.jimenez.ecuafit.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jimenez.ecuafit.data.entities.ComidaDB

@Dao
interface ComidaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComida(c: ComidaDB)
    @Query("select * from ComidaDB")
    fun getAllComida():List<ComidaDB>

}