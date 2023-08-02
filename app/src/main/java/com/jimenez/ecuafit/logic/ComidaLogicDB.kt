package com.jimenez.ecuafit.logic

import com.jimenez.ecuafit.data.entities.Comida
import com.jimenez.ecuafit.data.entities.ComidaDB
import com.jimenez.ecuafit.ui.utilities.EcuaFit
import java.util.Date

class ComidaLogicDB {
    suspend fun insertComida(item:Comida,cantidad:Int,fecha:Date){
        var ComidaData=



            ComidaDB(
                calorias = item.calorias,
                descripcion = item.descripcion,
                foto = item.foto,
                macronutrientes = item.macronutrientes,
                micronutrientes = item.micronutrientes,
                nombre = item.nombre,
                region = item.region,
                fecha = fecha,
                cantidad = cantidad
            )


        EcuaFit.getDbInstance().comidaDao().insertComida(ComidaData)
    }
}