package com.christhemar.notasroom.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.christhemar.notasroom.R
import com.christhemar.notasroom.db.Nota
import com.christhemar.notasroom.db.NotaDB
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialSharedAxis
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
        //Animaciones de entrada y salida
        sharedElementEnterTransition=MaterialContainerTransform()

        exitTransition=MaterialSharedAxis(MaterialSharedAxis.Z,true)

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

        //Obtener argumentos
        val bundle=arguments

        if(bundle!=null){
            val id=requireArguments().getInt("id")
            lifecycleScope.launch {
                val notaOb= withContext(Dispatchers.IO){helper.notaDao.findById(id)}
                titulo.append(notaOb.titulo)
                texto.append(notaOb.texto)
            }
        }


        toolbarSave.setNavigationOnClickListener {
            volver()
        }

        toolbarSave.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.guardar->{
                    if(validar(titulo.text.trim().toString(),texto.text.trim().toString())){
                        if(arguments==null){
                            lifecycleScope.launch {
                                //val longitudUltima:Int= withContext(Dispatchers.IO){ helper.notaDao.lastId() }
                                //var idAuto=longitudUltima+1
                                val fecha=obtenerHechaActual()
                                val nota=Nota(0,titulo.text.trim().toString(),texto.text.trim().toString(),fecha,0)
                                withContext(Dispatchers.IO){ helper.notaDao.insert(nota) }
                                volver()
                            }
                        }else{
                            lifecycleScope.launch {
                                val id=requireArguments().getInt("id")
                                val fecha=obtenerHechaActual()
                                val notaUpdate=Nota(id,titulo.text.trim().toString(),texto.text.trim().toString(),fecha,0)
                                withContext(Dispatchers.IO){ helper.notaDao.update(notaUpdate) }
                                volver()
                            }
                        }
                    }else{
                        Toast.makeText(context, "Ingrese su texto", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                R.id.colorSave->{
                    //Toast.makeText(context, "Paleta de colores", Toast.LENGTH_SHORT).show()
                    mostrarDialog()

                    true
                }
                else -> {false}
            }
        }

    }

    private fun mostrarDialog() {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //Indicando contenido
        dialog?.setContentView(R.layout.modal_bottom)
        //Obteniendo vistas del conteinido
        val tituloD = dialog!!.findViewById<TextView>(R.id.dialog_title)
        val img = dialog.findViewById<ImageView>(R.id.dialog_img)

        img.setOnClickListener {
            Toast.makeText(context, "Presiono imagen", Toast.LENGTH_SHORT).show()
        }

        tituloD.setOnClickListener {
            Toast.makeText(context, "Presiono Titulo", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
        //Ancho y alto
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        //Color de fondo del Dialog
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        //Animaciones implementadas en style
        dialog.window!!.attributes.windowAnimations=R.style.DialogAnimation
        //Indicando hubicacion del dialog
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }

    fun volver(){
        //Saca el estado superior de la pila posterior. Esta función es asincrónica: pone en
        // cola la solicitud para que aparezca, pero la acción no se realizará hasta que
        // la aplicación vuelva a su bucle de eventos.
        parentFragmentManager.popBackStack()
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

