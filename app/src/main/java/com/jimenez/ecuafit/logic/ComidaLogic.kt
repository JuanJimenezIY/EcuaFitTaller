package com.jimenez.ecuafit.logic

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.jimenez.ecuafit.data.Comida
import kotlinx.coroutines.tasks.await

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ComidaLogic {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getAllComida(): List<Comida> = withContext(Dispatchers.IO) {
        val comidaList = mutableListOf<Comida>()

        try {
            val querySnapshot = db.collection("comida").get().await()
            for (document in querySnapshot) {
                val m = Comida(
                    document.getLong("calorias")?.toInt() ?: 0,
                    document.getString("descripcion") ?: "",
                    document.getString("foto") ?: "",
                    document.getString("macronutrientes") ?: "",
                    document.getString("micronutrientes") ?: "",
                    document.getString("nombre") ?: "",
                    document.getString("region") ?: ""
                )
                comidaList.add(m)
            }
        } catch (e: Exception) {
            // Handle any exceptions that may occur during data retrieval
            // e.g., log the error or display an error message
        }

        comidaList
    }
}
