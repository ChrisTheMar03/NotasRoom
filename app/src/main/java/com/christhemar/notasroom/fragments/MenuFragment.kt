package com.christhemar.notasroom.fragments

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
import com.google.android.material.appbar.MaterialToolbar
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
        setUpToolbar(view)

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

    //Metodo para que funciones el buscar - Necesario
    private fun setUpToolbar(view: View) {
        val toolbar=view.findViewById<MaterialToolbar>(R.id.toolbarMenu)
        val activity=activity  as AppCompatActivity
        if(activity!=null){
            activity.setSupportActionBar(toolbar)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.toolbar_menu,menu)
        val menuItem=menu.findItem(R.id.buscar)
        val searchView:SearchView=menuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val listaFiltrada=listaNotas.filter {
                    it.titulo.toLowerCase().contains(newText!!.toLowerCase())
                }
                lifecycleScope.launch {
                    rv.adapter=NotaAdapter(listaFiltrada, { nota -> obtenerNota(nota)},{nota->elimnarNota(nota)})
                }

                return false
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
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