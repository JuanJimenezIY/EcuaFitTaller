package com.jimenez.ecuafit.logic

import android.content.Intent
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.jimenez.ecuafit.data.entities.Usuario
import com.jimenez.ecuafit.ui.activities.MenuActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UsuarioLogic {
    private val db = FirebaseFirestore.getInstance()

    suspend fun recuperarUsuario(correo: String) = withContext(Dispatchers.IO) {
        val documentSnapshot = db.collection("users").document(correo).get().await()
        val u = Usuario(
            documentSnapshot.getString("altura")?: "",
            documentSnapshot.getString("correo") ?: "",
            documentSnapshot.getString("edad") ?: "",
            documentSnapshot.getString("genero") ?: "",
            documentSnapshot.getString("nombre") ?: "",
            documentSnapshot.get("peso") as List<String>
        )

        UsuarioLogicDB().insertUsuario(u)
    }
}