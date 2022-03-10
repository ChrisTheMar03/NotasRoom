package com.christhemar.notasroom.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.christhemar.notasroom.R
import com.christhemar.notasroom.db.Nota
import com.google.android.material.card.MaterialCardView

class NotaViewHolder(view:View) : RecyclerView.ViewHolder(view) {

    private val titulo=view.findViewById<TextView>(R.id.item_titulo)
    private val texto=view.findViewById<TextView>(R.id.item_texto)
    private val fecha=view.findViewById<TextView>(R.id.item_fecha)
    private val cardview=view.findViewById<MaterialCardView>(R.id.item_cardView)

    fun render(nota: Nota,setOnClickListener:(Nota)->Unit,onLongClickListener:(Nota)->Unit){
        titulo.text=nota.titulo
        texto.text=nota.texto
        fecha.text=nota.fecha

        cardview .setOnClickListener {
            setOnClickListener(nota)
        }


        cardview.setOnLongClickListener(object:View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                onLongClickListener(nota)
                return true
            }
        })

    }

}