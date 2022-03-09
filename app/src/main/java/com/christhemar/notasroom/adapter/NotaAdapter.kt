package com.christhemar.notasroom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.christhemar.notasroom.R
import com.christhemar.notasroom.db.Nota

class NotaAdapter(private val listNotas:List<Nota>,
                  private val setOnClickListener:(Nota)->Unit,
                  private val onLongClickListener:(Nota)->Unit ) : RecyclerView.Adapter<NotaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val view=LayoutInflater.from(parent.context)
        return NotaViewHolder(view.inflate(R.layout.item_nota,parent,false))
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val item=listNotas[position]
        holder.render(item,setOnClickListener,onLongClickListener)
    }

    override fun getItemCount(): Int = listNotas.size

}