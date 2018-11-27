package projandroid.com.filmes.project.view

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_novo_filme.*
import projandroid.com.filmes.R
import projandroid.com.filmes.project.db.Filme

class NovoFilmeActivity : AppCompatActivity() {

    private var image_uri : Uri? = null
    private var mCurrentPhotoPath: String = ""
    lateinit var filme: Filme

    companion object {
        // image pick code
        private val REQUEST_IMAGE_GALLERY = 1000
        private val REQUEST_IMAGE_CAPTURE = 2000
        const val EXTRA_REPLY = "view.REPLY"
        const val EXTRA_DELETE = "view.DELETE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_filme)

        // botão de voltar ativo no menu superior esquerdo
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fAddFilme?.setOnClickListener {
            fAddFilme.setOnCreateContextMenuListener { menu, v, menuInfo ->
                menu.add(Menu.NONE, 1, Menu.NONE, "Escolher foto")
                menu.add(Menu.NONE, 2, Menu.NONE, "Tirar foto")
            }
        }

        val intent: Intent = intent

        try{
            filme = intent.getSerializableExtra(EXTRA_REPLY) as Filme
            filme.let {
                etNome.setText(filme.nome)
                etDescricao.setText(filme.descricao)
            }
        }catch(e: Exception){ }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }

    private fun takePicture() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "nova imagem")
        values.put(MediaStore.Images.Media.DESCRIPTION, "imagem da camera")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")

        image_uri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if(intent.resolveActivity(packageManager) != null) {
            mCurrentPhotoPath = image_uri.toString()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun getPermissionImageFromGallery(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                // permission denied
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permission, REQUEST_IMAGE_GALLERY)
            } else {
                // permission granted
                pickImageFromGallery()
            }
        }
        else{
            // system < M
            pickImageFromGallery()
        }
    }

    private fun getPermissionTakePicture(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                // permission denied
                val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, REQUEST_IMAGE_CAPTURE)
            } else {
                // permission granted
                takePicture()
            }
        }
        else{
            // system < M
            //takePicture()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_IMAGE_GALLERY -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) pickImageFromGallery()
                else Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
            }
            REQUEST_IMAGE_CAPTURE ->{
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) takePicture()
                else Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_GALLERY) imgNovoFilme.setImageURI(data?.data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) imgNovoFilme.setImageURI(image_uri)

    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            1 -> getPermissionImageFromGallery()
            2 -> getPermissionTakePicture()
        }
        return super.onContextItemSelected(item)
    }

    // ---- menu ----

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_novo_filme, menu)
        val menuItem = menu?.findItem(R.id.menu_deletar_filme)
        try{
            filme.let {
                menuItem?.isVisible = true
            }
        }catch(e: Exception){
            menuItem?.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when {
            item?.itemId == android.R.id.home -> {
                finish()
                true
            }
            item?.itemId == R.id.menu_salvar_filme -> {
                if(etNome.text.isNullOrEmpty()){
                    Toast.makeText(this, "Insira o nome do filme!", Toast.LENGTH_LONG).show()
                }else{
                    if (filme.id > 0){
                        filme.nome = etNome.text.toString()
                        filme.descricao = etDescricao.text.toString()
                    }
                    else{
                        val filme = Filme(nome = etNome.text.toString(), descricao = etDescricao.text.toString())
                    }
                    val replyIntent = Intent()
                    replyIntent.putExtra(EXTRA_REPLY, filme)
                    setResult(Activity.RESULT_OK, replyIntent)
                }
                finish()
                true
            }
            item?.itemId == R.id.menu_deletar_filme -> {
                val replyIntent = Intent()
                replyIntent.putExtra(EXTRA_DELETE, filme)
                setResult(Activity.RESULT_OK, replyIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}