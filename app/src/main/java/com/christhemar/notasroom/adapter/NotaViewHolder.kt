package com.christhemar.notasroom.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.christhemar.notasroom.R
import com.christhemar.notasroom.db.Nota

class NotaViewHolder(view:View) : RecyclerView.ViewHolder(view) {

    private val titulo=view.findViewById<TextView>(R.id.item_titulo)
    private val texto=view.findViewById<TextView>(R.id.item_texto)
    private val fecha=view.findViewById<TextView>(R.id.item_fecha)

    fun render(nota: Nota){
        titulo.text=nota.titulo
        texto.text=nota.texto
        fecha.text=nota.fecha
    }

}