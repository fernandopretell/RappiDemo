package com.fernandopretell.rappidemo.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.fernandopretell.componentes.list_bienvenida.Lista_bienvenida
import com.fernandopretell.componentes.list_bienvenida.models.ItemBienvenida
import com.fernandopretell.rappidemo.R
import com.fernandopretell.rappidemo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_bienvenida.*

//Activity sin interaccion con datos remotos
class BienvenidaActivity : BaseActivity() {

    private val THE_MARVEL_UNIVERSE = "The Marvel Universe"
    private val OSCAR_2012 = "2012 Oscar Nominations"
    private val DC_COMICS = "The DC Comics Universe"
    private val OSCAR_2011 = "2011 Oscar Nominations"
    private val THE_ADVENGERS = "The Avengers"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        val listaTemas = arrayListOf<ItemBienvenida>()

        val tema1 = ItemBienvenida(THE_MARVEL_UNIVERSE, 1)
        val tema2 = ItemBienvenida(OSCAR_2012, 2)
        val tema3 = ItemBienvenida(DC_COMICS, 3)
        val tema4 = ItemBienvenida(OSCAR_2011, 4)
        val tema5 = ItemBienvenida(THE_ADVENGERS, 5)

        listaTemas.add(tema1)
        listaTemas.add(tema2)
        listaTemas.add(tema3)
        listaTemas.add(tema4)
        listaTemas.add(tema5)

        lb.updateList(listaTemas)

        lb.bienvenidaListener = object : Lista_bienvenida.BienvenidaListener {

            override fun pressedItem(item: ItemBienvenida, position: Int) {

                val intent = Intent(this@BienvenidaActivity, MainActivity::class.java)
                intent.putExtra("id", item.imagenId)
                startActivity(intent)
            }
        }

        verifyStoragePermissions(this)

    }

    private fun verifyStoragePermissions(activity: Activity): Boolean {
        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val REQUEST_EXTERNAL_STORAGE = 1
        val permission =
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
            return false
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    applicationContext,
                    "Gracias por darnos los permisos.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                //runtime_permissions();
            }
        }
    }

    override fun showNetworkMessage(isConnected: Boolean) {
        //No se necesita implementar ya que esta actividad trabaj fuera de linea
    }
}
