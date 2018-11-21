package projandroid.com.produtos.project.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_lista_celular.view.*
import projandroid.com.produtos.R
import projandroid.com.produtos.project.db.Celular

class CelularRecyclerAdapter internal constructor(context: Context) :
RecyclerView.Adapter<CelularRecyclerAdapter.ViewHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var celulares = emptyList<Celular>()

    override fun onCreateViewHolder(holder: ViewGroup, position: Int): CelularRecyclerAdapter.ViewHolder {
        val view = inflater.inflate(R.layout.item_lista_celular, holder, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = celulares.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = celulares[position]
        holder.modeloCelular.text = current.modelo
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val modeloCelular: TextView = itemView.txtCelularListaModelo
    }

    fun setListaCelulares(listaCelulares: List<Celular>) {
        this.celulares = listaCelulares
        notifyDataSetChanged()
    }
}