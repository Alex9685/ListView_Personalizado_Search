package com.example.estacochinada
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: CustomListAdapter
    private lateinit var arrayList: ArrayList<String>
    private lateinit var filteredList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        listView = findViewById(R.id.lstNombres)

        arrayList = ArrayList(resources.getStringArray(R.array.array_nombres).toList())
        filteredList = ArrayList(arrayList)

        adapter = CustomListAdapter(this, R.layout.list_item, filteredList)
        listView.adapter = adapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedName = filteredList[position]
            Toast.makeText(this@MainActivity, "Seleccionó el alumno: $selectedName", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.searchview, menu)
        val searchItem: MenuItem = menu.findItem(R.id.menu_search)

        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filteredList.clear()
                if (newText.isEmpty()) {
                    filteredList.addAll(arrayList)
                } else {
                    val filterPattern = newText.toLowerCase().trim()
                    for (name in arrayList) {
                        if (name.toLowerCase().contains(filterPattern)) {
                            filteredList.add(name)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    inner class CustomListAdapter(
        context: Context,
        private val layoutResourceId: Int,
        private val data: ArrayList<String>
    ) : ArrayAdapter<String>(context, layoutResourceId, data), Filterable {

        private var originalData: ArrayList<String> = ArrayList(data)

        override fun getCount(): Int {
            return data.size
        }

        override fun getItem(position: Int): String? {
            return data[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var row = convertView
            val holder: ViewHolder

            if (row == null) {
                val inflater = LayoutInflater.from(context)
                row = inflater.inflate(layoutResourceId, parent, false)

                holder = ViewHolder()
                holder.imgFoto = row.findViewById(R.id.imgFoto)
                holder.txtNombre = row.findViewById(R.id.txtNombre)
                holder.txtMatricula = row.findViewById(R.id.txtMatricula)

                row.tag = holder
            } else {
                holder = row.tag as ViewHolder
            }

            val name = data[position]
            holder.txtNombre?.text = name
            holder.txtMatricula?.text = resources.getStringArray(R.array.array_matriculas)[position]

            val imagenNombre = "img_alumno" + (position + 1)
            val imagenId = context.resources.getIdentifier(imagenNombre, "drawable", context.packageName)

            if (imagenId != 0) {
                holder.imgFoto?.setImageResource(imagenId)
            } else {
                holder.imgFoto?.setImageResource(R.drawable.img_alumno1)
            }

            return row!!
        }

        inner class ViewHolder {
            internal var imgFoto: ImageView? = null
            internal var txtNombre: TextView? = null
            internal var txtMatricula: TextView? = null
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence): FilterResults {
                    val results = FilterResults()

                    val filteredList = ArrayList<String>()

                    if (constraint.isEmpty()) {
                        filteredList.addAll(originalData)
                    } else {
                        val filterPattern = constraint.toString().toLowerCase().trim()

                        for (name in originalData) {
                            if (name.toLowerCase().contains(filterPattern)) {
                                filteredList.add(name)
                            }
                        }
                    }

                    results.values = filteredList
                    results.count = filteredList.size
                    return results
                }

                override fun publishResults(constraint: CharSequence, results: FilterResults) {
                    filteredList.clear()
                    filteredList.addAll(results.values as ArrayList<String>)
                    notifyDataSetChanged()
                }
            }
        }
    }
}
