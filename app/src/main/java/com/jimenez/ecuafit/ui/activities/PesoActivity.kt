package com.jimenez.ecuafit.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import androidx.lifecycle.lifecycleScope
import com.jimenez.ecuafit.R
import com.jimenez.ecuafit.databinding.ActivityAguaBinding
import com.jimenez.ecuafit.databinding.ActivityPesoBinding
import com.jimenez.ecuafit.ui.utilities.EcuaFit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PesoActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPesoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPesoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recoverPeso()
    }
    fun recoverPeso(){
        lifecycleScope.launch(Dispatchers.IO){
            var u=EcuaFit.getDbUsuarioInstance().usuarioDao().getAll()
            binding.peso.text= Editable.Factory.getInstance().newEditable(u.peso[0])

        }
    }
}