package com.jimenez.ecuafit.ui.utilities

import com.jimenez.ecuafit.data.entities.UsuarioDB
import java.math.RoundingMode

class Calculos {
    fun sumLista(lista:List<Double>):Double{
        return lista.sumOf { it }
    }
    companion object {
        fun round(num: Double): Double {
            return num.toBigDecimal().setScale(0, RoundingMode.UP).toDouble()
        }
        fun calcularTMB(usuario: UsuarioDB): Double {
            if (usuario.genero.contains("mas")) {
                return 66.47 + ((13.75 * usuario.peso[usuario.peso.size - 1].toDouble()) + (5 * usuario.altura.toDouble()) - (6.76 * usuario.edad.toDouble()))
            } else {
                return 65.51 + ((9.56 * usuario.peso[usuario.peso.size - 1].toDouble()) + (1.85 * usuario.altura.toDouble()) - (4.68 * usuario.edad.toDouble()))
            }
        }
        fun calcularPorcentaje(caloriasRestantes:Double,calsTotales:Double):Double{
            return round((100 - ((caloriasRestantes * 100) / calsTotales)))
        }
    }
}