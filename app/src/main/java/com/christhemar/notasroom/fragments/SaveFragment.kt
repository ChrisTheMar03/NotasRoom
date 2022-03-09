package com.christhemar.notasroom.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.christhemar.notasroom.R
import com.christhemar.notasroom.db.Nota
import com.christhemar.notasroom.db.NotaDB
import kotlinx.android.synthetic.main.fragment_save.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class SaveFragment : Fragment() {

    private lateinit var helper:NotaDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        GlobalScope.launch(Dispatchers.IO) {
            helper=context?.let {
                Room.databaseBuilder(it,NotaDB::class.java,NotaDB.DB_NAME).allowMainThreadQueries().build()
            }!!
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_save,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val titulo=view.findViewById<EditText>(R.id.s_titulo)
        val texto=view.findViewById<EditText>(R.id.s_texto)

        toolbarSave.setNavigationOnClickListener {
            volver()
        }

        toolbarSave.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.guardar->{
                    if(validar(titulo.text.trim().toString(),texto.text.trim().toString())){
                        lifecycleScope.launch {
                            var longitudUltima:Int= withContext(Dispatchers.IO){ helper.notaDao.lastId() }
                            var idAuto=longitudUltima+1
                            val fecha=obtenerHechaActual()
                            val nota=Nota(idAuto,titulo.text.trim().toString(),texto.text.trim().toString(),fecha,0)
                            withContext(Dispatchers.IO){ helper.notaDao.insert(nota) }
                        }
                    }else{
                        Toast.makeText(context, "Ingrese su texto", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                R.id.colorSave->{
                    Toast.makeText(context, "Paleta de colores", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {false}
            }
        }

    }

    fun volver(){
        //Saca el estado superior de la pila posterior. Esta función es asincrónica: pone en
        // cola la solicitud para que aparezca, pero la acción no se realizará hasta que
        // la aplicación vuelva a su bucle de eventos.
        activity?.supportFragmentManager!!.popBackStack()
    }

    fun validar(titulo:String,texto:String):Boolean{
        var valido=false
        if(!titulo.equals("") || !texto.equals("")){
            valido=true
        }
        return valido
    }

    fun obtenerHechaActual():String{
        return SimpleDateFormat("yyyy-MM-dd").format(Date()).toString()
    }

}