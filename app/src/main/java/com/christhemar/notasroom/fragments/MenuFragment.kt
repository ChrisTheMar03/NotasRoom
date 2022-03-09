package com.christhemar.notasroom.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.christhemar.notasroom.R
import com.christhemar.notasroom.adapter.NotaAdapter
import com.christhemar.notasroom.db.Nota
import com.christhemar.notasroom.db.NotaDB
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuFragment : Fragment(R.layout.fragment_menu) {

    private lateinit var helper:NotaDB
    private lateinit var listaNotas:List<Nota>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        GlobalScope.launch(Dispatchers.IO) {
            helper= context?.let { Room.databaseBuilder(it,NotaDB::class.java,NotaDB.DB_NAME).allowMainThreadQueries().build() }!!
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv.layoutManager=LinearLayoutManager(context)

        //Data - RecyclerView
        //Predeterminadamente usa el Disp.Main
        lifecycleScope.launch {
            val job= withContext(Dispatchers.IO){helper.notaDao.getAll()}
            listaNotas=job
            withContext(Dispatchers.Main){ mostrar(listaNotas) }
        }

        //Redireccionar
        btnSave.setOnClickListener {
            direccionar()
        }

    }

    fun direccionar(){
        activity?.supportFragmentManager!!
            .beginTransaction()
            .setCustomAnimations(R.anim.anim_dentro,R.anim.anim_salida,R.anim.anim_pop_dentro,R.anim.anim_pop_fuera)
            .replace(R.id.container,SaveFragment())
            .addToBackStack(null)
            .commit()
    }

    fun mostrar(lista:List<Nota>){
        if(!lista.isEmpty()){
            rv.adapter=NotaAdapter(lista)
        }
    }


    fun mostrar(){
        //val nota=helper.notaDao.findById(1)
        //Toast.makeText(context, "Datos: ${nota.titulo}", Toast.LENGTH_SHORT).show()
    }

}