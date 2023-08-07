package com.jimenez.ecuafit.logic

import com.jimenez.ecuafit.data.entities.Comida
import com.jimenez.ecuafit.data.entities.ComidaDB
import com.jimenez.ecuafit.ui.utilities.EcuaFit
import java.util.Date
import kotlin.streams.toList

class ComidaLogicDB {
    suspend fun insertComida(item:Comida,cantidad:Int,fecha:Date){
        var ComidaData=
            ComidaDB(
                calorias = item.calorias*cantidad,
                descripcion = item.descripcion,
                foto = item.foto,
                macronutrientes = item.macronutrientes.stream().map { x->((x.toDouble())*cantidad).toString() }.toList(),
                micronutrientes = item.micronutrientes,
                nombre = item.nombre,
                region = item.region,
                fecha = fecha,
                cantidad = cantidad
            )
        EcuaFit.getDbInstance().comidaDao().insertComida(ComidaData)
    }
    suspend fun getAllComida():List<ComidaDB>{
        return EcuaFit.getDbInstance().comidaDao().getAllComida();
    }
    suspend fun getAllComidaByFecha(fecha:Long):List<ComidaDB>{
        return EcuaFit.getDbInstance().comidaDao().getAllComidaByDia(fecha);
    }
}