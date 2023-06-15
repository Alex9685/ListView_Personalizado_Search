package com.example.estacochinada

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ListViewAdapter(
    private val context: Activity,
    private val layoutResourceId: Int,
    private val list: ArrayList<AlumnoData>
) : ArrayAdapter<AlumnoData>(context, layoutResourceId, list) {

    private val inflater: LayoutInflater = context.layoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        val holder: ViewHolder

        if (row == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            row = inflater.inflate(layoutResourceId, parent, false)

            holder = ViewHolder()
            holder.imgFoto = row.findViewById(R.id.imgFoto)
            holder.txtNombre = row.findViewById(R.id.txtNombre)
            holder.txtMatricula = row.findViewById(R.id.txtMatricula)

            row.tag = holder
        } else {
            holder = row.tag as ViewHolder
        }

        val alumno = list[position]
        holder.txtNombre.text = alumno.nombreCompleto
        holder.txtMatricula.text = alumno.matricula

        // Obtener el nombre de la imagen
        val imagenNombre = "img_" + alumno.matricula

        // Obtener el ID de la imagen correspondiente
        val imagenId = context.resources.getIdentifier(imagenNombre, "drawable", context.packageName)

        // Establecer la imagen en holder.imgFoto
        if (imagenId != 0) {
            holder.imgFoto.setImageResource(imagenId)
        } else {
            // Si no se encuentra la imagen, puedes establecer una imagen predeterminada o dejarla en blanco

        }

        return row!!
    }

    class ViewHolder {
        lateinit var imgFoto: ImageView
        lateinit var txtNombre: TextView
        lateinit var txtMatricula: TextView
    }
}
