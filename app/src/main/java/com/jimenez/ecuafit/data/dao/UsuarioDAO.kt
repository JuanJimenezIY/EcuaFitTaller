package com.jimenez.ecuafit.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jimenez.ecuafit.data.entities.UsuarioDB

@Dao
interface UsuarioDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsuario(u: UsuarioDB)
    @Query("select * from UsuarioDB where correo=:correo")
    fun getUsuarioByCorreo(correo:String):UsuarioDB

    @Query("select * from UsuarioDB")
    fun getAll():UsuarioDB

    @Query("UPDATE UsuarioDB SET peso=:peso WHERE id = :id")
    fun update(peso: Int?, id: Int)
    @Query("DELETE FROM UsuarioDB")
    fun deleteAll():Int
}
