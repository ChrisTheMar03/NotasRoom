package com.christhemar.notasroom.fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.christhemar.notasroom.R
import com.christhemar.notasroom.adapter.NotaAdapter
import com.christhemar.notasroom.db.Nota
import com.christhemar.notasroom.db.NotaDB
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            mostrar(listaNotas)
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
            rv.adapter=NotaAdapter(lista, { nota -> obtenerNota(nota)},{nota->elimnarNota(nota)})
        }

    }

    fun obtenerNota(nota: Nota){
        //Toast.makeText(context, "Titulo: ${nota.titulo}", Toast.LENGTH_SHORT).show()
        val bundle= Bundle().apply {
            putInt("id",nota.id)
        }

        activity?.supportFragmentManager!!
            .beginTransaction()
            .setCustomAnimations(R.anim.anim_dentro,R.anim.anim_salida,R.anim.anim_pop_dentro,R.anim.anim_pop_fuera)
            .replace(R.id.container,SaveFragment::class.java,bundle)
            .addToBackStack(null)
            .commit()

        //Toast.makeText(context, "${bundle}", Toast.LENGTH_SHORT).show()
    }

    fun elimnarNota(nota: Nota){
        //Toast.makeText(context, "Nota: ${nota.id}", Toast.LENGTH_SHORT).show()
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Eliminar Nota")
                .setMessage("Desea eliminar esta nota ?")
                .setNegativeButton("Cancelar",null)
                .setPositiveButton("Eliminar",{ dialog,cambio ->
                    eliminarItem(nota.id)
                })
                .show()
        }
    }

    fun eliminarItem(id:Int){
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val notaoB = helper.notaDao.findById(id)
                helper.notaDao.delete(notaoB)
            }
            rv.adapter=NotaAdapter(helper.notaDao.getAll(), { nota -> obtenerNota(nota)},{nota->elimnarNota(nota)})
        }

    }

    /*
    fun ocultarTeclado(){
        val view:View?=activity?.currentFocus
        if(view != null){
            val inputStream=activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputStream.hideSoftInputFromWindow(view.windowToken,0)
        }

    }*/

}