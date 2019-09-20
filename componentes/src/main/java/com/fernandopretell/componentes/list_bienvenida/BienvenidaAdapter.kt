package com.fernandopretell.componentes.list_bienvenida

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.fernandopretell.componentes.R
import com.fernandopretell.componentes.list_bienvenida.models.ItemBienvenida

class BienvenidaAdapter(private val listener: bienvenidaItemListener): RecyclerView.Adapter<BienvenidaAdapter.BienvenidaViewHolder>() {

    private val data: ArrayList<ItemBienvenida> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BienvenidaViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_bienvenida, parent, false)
        return BienvenidaViewHolder(v,data)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: BienvenidaViewHolder, position: Int) {
        val item_bienvenida = data.get(position)

        holder.nombre.text = item_bienvenida.nombre
        when(item_bienvenida.imagenId){
            1 -> holder.imagen.setImageResource(R.drawable.tema1)
            2 -> holder.imagen.setImageResource(R.drawable.tema2)
            3 -> holder.imagen.setImageResource(R.drawable.tema3)
            4 -> holder.imagen.setImageResource(R.drawable.tema4)
            5 -> holder.imagen.setImageResource(R.drawable.tema5)
        }

        holder.contenedor.setOnClickListener {
            listener.pressedItem(item_bienvenida,position)
        }
    }

    fun updateData(newData: List<ItemBienvenida>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    inner class BienvenidaViewHolder(itemView: View, data: ArrayList<ItemBienvenida>) : RecyclerView.ViewHolder(itemView) {

        val nombre: TextView
        val imagen: ImageView
        val contenedor: CardView

        init {
            nombre = itemView.findViewById(R.id.tvNombreTema)
            imagen = itemView.findViewById(R.id.ivImagenTema)
            contenedor = itemView.findViewById(R.id.contenedorBienvenida)
        }
    }
}

interface bienvenidaItemListener {

    fun pressedItem(item: ItemBienvenida, position: Int)

}