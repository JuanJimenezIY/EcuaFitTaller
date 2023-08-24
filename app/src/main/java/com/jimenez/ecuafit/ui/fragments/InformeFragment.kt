package com.jimenez.ecuafit.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.jimenez.ecuafit.R
import com.jimenez.ecuafit.data.entities.ComidaDB
import com.jimenez.ecuafit.data.entities.Usuario
import com.jimenez.ecuafit.data.entities.UsuarioDB
import com.jimenez.ecuafit.databinding.ActivityAguaBinding
import com.jimenez.ecuafit.databinding.ActivityPesoBinding
import com.jimenez.ecuafit.databinding.FragmentInformeBinding
import com.jimenez.ecuafit.logic.ComidaLogicDB
import com.jimenez.ecuafit.ui.activities.AguaActivity
import com.jimenez.ecuafit.ui.activities.ComidaDiariaActivity
import com.jimenez.ecuafit.ui.activities.EjerciciosActivity
import com.jimenez.ecuafit.ui.activities.MainActivity
import com.jimenez.ecuafit.ui.activities.PesoActivity
import com.jimenez.ecuafit.ui.activities.RegistroActivity
import com.jimenez.ecuafit.ui.utilities.EcuaFit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale


class InformeFragment : Fragment() {

    private lateinit var binding: FragmentInformeBinding
    private var comidaItems: MutableList<ComidaDB> = mutableListOf();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentInformeBinding.inflate(layoutInflater)
        chargeData()
    }

    fun chargeData() {
        val localDateTime = LocalDateTime.now()
        val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
        val date = Date.from(instant)
        var task = lifecycleScope.launch(Dispatchers.Main) {
            comidaItems = withContext(Dispatchers.IO) {
                return@withContext ComidaLogicDB().getAllComidaByFecha(date)
            } as MutableList<ComidaDB>
            var sumaCalorias = comidaItems.sumOf { it.calorias }
            var sumaGrasas = comidaItems.sumOf { it.macronutrientes[0].toDouble() }
            var sumaProteinas = comidaItems.sumOf { it.macronutrientes[1].toDouble() }
            var sumaCarbs = comidaItems.sumOf { it.macronutrientes[2].toDouble() }
            binding.calsConsumidas.text = "Consumidas: " + sumaCalorias + " kcals"
            binding.proteinasCons.text =
                sumaProteinas.toBigDecimal().setScale(0, RoundingMode.UP).toString()
            binding.carbsCons.text =
                sumaCarbs.toBigDecimal().setScale(0, RoundingMode.UP).toString()
            binding.grasaCons.text =
                sumaGrasas.toBigDecimal().setScale(0, RoundingMode.UP).toString()

            // Cambio a Dispatchers.Main
            val calsTotales = withContext(Dispatchers.IO) {
                calcular(EcuaFit.getDbUsuarioInstance().usuarioDao().getAll()).toString()
            }
            val caloriasRestantes = calsTotales.toDouble() - sumaCalorias
            binding.calsRestantes.text =
                String.format("%.2f", caloriasRestantes) // Modificación en el hilo principal
            binding.procentaje.text =
                (100 - ((caloriasRestantes * 100) / calsTotales.toDouble())).toBigDecimal()
                    .setScale(0, RoundingMode.UP).toString()
            if (binding.calsRestantes.text.toString()
                    .toDouble() < 0 && binding.procentaje.text.toString().toDouble() < 0
            ) {
                binding.calsRestantes.text = "0"
                binding.procentaje.text = "0"
            }
            if(binding.procentaje.text.toString().toDouble()>100){
                binding.procentaje.text="100"
            }
            binding.proteinasRes.text =
                ((calsTotales.toDouble() * 0.30) / 4).toBigDecimal().setScale(0, RoundingMode.UP)
                    .toString()
            binding.carbsRes.text =
                ((calsTotales.toDouble() * 0.45) / 4).toBigDecimal().setScale(0, RoundingMode.UP)
                    .toString()
            binding.grasaRes.text =
                ((calsTotales.toDouble() * 0.25) / 9).toBigDecimal().setScale(0, RoundingMode.UP)
                    .toString()

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //var comidaDiaria=
        // Configurar el click listener para el CardView de Peso
        binding.pesoText.setOnClickListener {
            val intent = Intent(requireContext(), PesoActivity::class.java)
            startActivity(intent)
        }
        binding.infDiario.setOnClickListener {
            val intent = Intent(requireContext(), ComidaDiariaActivity::class.java)
            startActivity(intent)
        }
        binding.logOut.setOnClickListener {
            logOut()
        }
        binding.cardEjercicios.setOnClickListener {
            val intent = Intent(requireContext(), EjerciciosActivity::class.java)
            startActivity(intent)

        }

    }

    @SuppressLint("SuspiciousIndentation")
    fun logOut() {

        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                EcuaFit.getDbUsuarioInstance().usuarioDao().deleteAll()
                EcuaFit.getDbInstance().comidaDao().deleteAll()
            }
        }
        val sharedPref = requireContext().getSharedPreferences("sesion", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("estado_usu", false)
                .apply()
        }
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    private fun calcular(usuario: UsuarioDB): Double {
        if (usuario.genero.contains("masculino")||usuario.genero.contains("Masculino")) {
            return (88.363 + ((13.397 * usuario.peso[usuario.peso.size - 1].toDouble()) + (4.7 * usuario.altura.toDouble()) - (5.66 * usuario.edad.toDouble())))*1.5
        } else {
            return (447.593 + ((9.247 * usuario.peso[usuario.peso.size - 1].toDouble()) + (3.08 * usuario.altura.toDouble()) - (4.68 * usuario.edad.toDouble())))*1.5
        }
    }

}