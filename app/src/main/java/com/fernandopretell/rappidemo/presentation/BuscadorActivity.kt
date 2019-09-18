package com.fernandopretell.rappidemo.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fernandopretell.componentes.carousel.Card_pelicula
import com.fernandopretell.rappidemo.R
import kotlinx.android.synthetic.main.activity_buscador.*
import java.util.ArrayList

class BuscadorActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscador)

        overridePendingTransitionEnter()

        var item = arrayListOf<Card_pelicula>()

        searchBuscarPelicula.setActivated(true)
        searchBuscarPelicula.setQueryHint("Buscar publicación")
        searchBuscarPelicula.onActionViewExpanded()
        searchBuscarPelicula.setIconified(false)
        searchBuscarPelicula.setEnabled(false)

        searchBuscarPelicula.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val peliculasList = ArrayList<Card_pelicula>()
                for (publicacion in item) {
                    if (publicacion.getTitulo().toLowerCase().contains(query)) {
                        peliculasList.add(publicacion)
                    }
                }

                //publicacionList.or

                adapter = BuscadorAdapter(peliculasList, this@BuscadorActivity)
                val lim = LinearLayoutManager(this@BuscadorActivity)
                lim.orientation = RecyclerView.VERTICAL
                rvBuscarPelicula.setLayoutManager(lim)
                rvBuscarPelicula.setAdapter(adapter)
                adapter.setClickListener(f)
                progressBarBuscar.setVisibility(View.INVISIBLE)
                searchBuscarPelicula.setEnabled(true)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }
        })
    }


    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected fun overridePendingTransitionEnter() {
        overridePendingTransition(R.animator.slide_from_right, R.animator.slide_to_left)
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected fun overridePendingTransitionExit() {
        overridePendingTransition(R.animator.slide_from_left, R.animator.slide_to_right)
    }
}
