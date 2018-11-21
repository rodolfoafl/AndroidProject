package projandroid.com.produtos.project.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_lista_celular.*
import projandroid.com.produtos.R
import projandroid.com.produtos.project.adapter.CelularRecyclerAdapter
import projandroid.com.produtos.project.viewmodel.CelularViewModel

class ListaCelularActivity : AppCompatActivity() {

    private lateinit var celularViewModel: CelularViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_celular)

        val recyclerView = rvListaCelular
        val adapter = CelularRecyclerAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        celularViewModel =
                ViewModelProviders.of(this).
                        get(CelularViewModel::class.java)

        celularViewModel.listaCelular.observe(this, Observer {celulares->
            celulares?.let { adapter.setListaCelulares(it) }
        })
    }
}