package com.jimenez.ecuafit.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comida(
    val calorias: Int,
    val descripcion: String,
    val foto: String,
    val macronutrientes: String,
    val micronutrientes: String,
    val nombre: String,
    val region: String
):Parcelable