package com.jimenez.ecuafit.ui.utilities

import android.app.Application
import androidx.room.Room
import com.jimenez.ecuafit.data.connections.ComidaConnectionDB

class EcuaFit:Application() {
    val name_class:String="Admin"
    override fun onCreate() {
        super.onCreate()
        db= Room.databaseBuilder(applicationContext,ComidaConnectionDB::class.java,"comidaDB").build()

    }

    companion object{
        val name_companion:String="Admin"
        private var db:ComidaConnectionDB ?= null
        fun getDbInstance():ComidaConnectionDB{
            return db!!
        }

    }
}