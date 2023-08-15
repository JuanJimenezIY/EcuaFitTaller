package com.jimenez.ecuafit.ui.fragments

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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InformeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InformeFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var binding: FragmentInformeBinding
    private var comidaItems: MutableList<ComidaDB> = mutableListOf();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= FragmentInformeBinding.inflate(layoutInflater)
        chargeData()



    }

    fun chargeData(){
        val localDateTime = LocalDateTime.now()
        val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
        val date = Date.from(instant)
        var task=lifecycleScope.launch(Dispatchers.Main){
            comidaItems= withContext(Dispatchers.IO){

                return@withContext ComidaLogicDB().getAllComidaByFecha(date)

            } as MutableList<ComidaDB>
            var sumaCalorias=comidaItems.sumOf { it.calorias }
            var sumaGrasas=comidaItems.sumOf { it.macronutrientes[0].toDouble()   }

            var sumaProteinas=comidaItems.sumOf { it.macronutrientes[1].toDouble() }

            var sumaCarbs=comidaItems.sumOf { it.macronutrientes[2].toDouble()  }

            binding.calsConsumidas.text="Consumidas: " +sumaCalorias+ " kcals"
            binding.proteinasCons.text=sumaProteinas.toBigDecimal().setScale(0, RoundingMode.UP).toString()
            binding.carbsCons.text=sumaCarbs.toBigDecimal().setScale(0,RoundingMode.UP).toString()
            binding.grasaCons.text=sumaGrasas.toBigDecimal().setScale(0,RoundingMode.UP).toString()

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
            val intent=Intent(requireContext(),ComidaDiariaActivity::class.java)
            startActivity(intent)
        }
        binding.logOut.setOnClickListener{
            logOut()
        }
        lifecycleScope.launch(Dispatchers.Main) { // Cambio a Dispatchers.Main
            val calsRestantes = withContext(Dispatchers.IO) {
                calcular(EcuaFit.getDbUsuarioInstance().usuarioDao().getAll()).toString()
            }

            binding.calsRestantes.text = calsRestantes // Modificación en el hilo principal
        }





    }
     fun logOut(){


            lifecycleScope.launch (Dispatchers.Main){
                withContext(Dispatchers.IO){
                    EcuaFit.getDbUsuarioInstance().usuarioDao().deleteAll()
                }
            }
            val sharedPref=requireContext().getSharedPreferences("sesion", Context.MODE_PRIVATE)
         Log.d("UCE",sharedPref.getBoolean("estado_usu",false).toString())
         with(sharedPref.edit()){
             putBoolean("estado_usu",false)
                 .apply()
         }
         Log.d("UCE",sharedPref.getBoolean("estado_usu",false).toString())

         val intent=Intent(requireContext(),MainActivity::class.java)
            startActivity(intent)


    }
    private fun calcular(usuario: UsuarioDB):Double{


        if(usuario.genero.contains("mas")){
            return 66.573+((13.751*usuario.peso[0].toDouble())+(5.0033*usuario.altura.toDouble())-(6.55*usuario.edad.toDouble()))
        }
        else{
            return 0.1
        }
    }

}