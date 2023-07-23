package com.jimenez.ecuafit.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jimenez.ecuafit.R
import com.jimenez.ecuafit.data.Comida
import com.jimenez.ecuafit.databinding.ComidaItemBinding
import com.squareup.picasso.Picasso


class ComidaAdapter(


    private var fnClick: (Comida) -> Unit
) :

    RecyclerView.Adapter<ComidaAdapter.ComidaViewHolder>() {

    var items: List<Comida> = listOf()

    class ComidaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ComidaItemBinding = ComidaItemBinding.bind(view)

        fun render(
            item: Comida,
            fnClick: (Comida) -> Unit
        ) {
            binding.txtNombre.text = item.nombre
            binding.txtCalorias.text = item.calorias.toString()
            Picasso.get().load(item.foto).into(binding.imgComida)




            itemView.setOnClickListener {
                fnClick(item)
            }
            //boton vista marvel chars
            //  binding.b{
            //    fnSave(item)
            //}


        }
    }




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ComidaAdapter.ComidaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ComidaViewHolder(
            inflater.inflate(
                R.layout.comida_item,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ComidaAdapter.ComidaViewHolder, position: Int) {
        holder.render(items[position], fnClick)
    }

    override fun getItemCount(): Int = items.size

    fun updateListItems(newItems: List<Comida>) {
        this.items = this.items.plus(newItems)
        notifyDataSetChanged()

    }

    fun replaceListAdapter(newItems: List<Comida>) {
        this.items = newItems
        notifyDataSetChanged()

    }

}