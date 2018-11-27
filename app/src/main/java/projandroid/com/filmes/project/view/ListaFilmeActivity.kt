package projandroid.com.filmes.project.view

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_lista_filme.*
import projandroid.com.filmes.R
import projandroid.com.filmes.project.adapter.FilmeRecyclerAdapter
import projandroid.com.filmes.project.db.Filme
import projandroid.com.filmes.project.viewmodel.FilmeViewModel

class ListaFilmeActivity : AppCompatActivity() {

    private lateinit var filmeViewModel: FilmeViewModel
    private val requestCodeAddFilme = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_filme)

        val recyclerView = rvListaFilme
        var adapter = FilmeRecyclerAdapter(this)
        recyclerView.adapter = adapter

        adapter.onItemClick = {
           val intent = Intent(this@ListaFilmeActivity, NovoFilmeActivity::class.java)
           intent.putExtra(NovoFilmeActivity.EXTRA_REPLY, it)
           startActivityForResult(intent, requestCodeAddFilme)
            //filmeViewModel.delete(it)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)

        filmeViewModel =
                ViewModelProviders.of(this).
                        get(FilmeViewModel::class.java)

        filmeViewModel.listaFilme.observe(this, Observer {filmes->
            filmes?.let { adapter.setListaFilmes(it) }
        })

        fbAdicionarFilme.setOnClickListener{
            val intent = Intent(this@ListaFilmeActivity, NovoFilmeActivity::class.java)
            startActivityForResult(intent, requestCodeAddFilme)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == requestCodeAddFilme && resultCode == Activity.RESULT_OK){
            data.let {
                try{
                    val filme: Filme = data?.getSerializableExtra(NovoFilmeActivity.EXTRA_REPLY) as Filme
                    if (filme.id > 0) {
                        filmeViewModel.update(filme)
                    } else {
                        filmeViewModel.insert(filme)
                    }
                }catch (e:Exception){
                    val filme: Filme = data?.getSerializableExtra(NovoFilmeActivity.EXTRA_DELETE) as Filme
                    filmeViewModel.delete(filme)
                }
            }
        }else{
            Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
        }
    }
}

