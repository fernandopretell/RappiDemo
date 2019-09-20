
package com.fernandopretell.rappidemo.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.fernandopretell.rappidemo.R
import com.fernandopretell.rappidemo.base.BaseActivity
import com.fernandopretell.rappidemo.model.CardModelParcelable
import com.fernandopretell.rappidemo.util.ConnectivityReceiver
import com.fernandopretell.rappidemo.util.Constants
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_vista_detalle.*
import kotlinx.android.synthetic.main.content_main_scrolling.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class VistaDetalleActivity : BaseActivity() {

    private var snackBar: Snackbar? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_detalle)

        val card =intent?.getParcelableExtra<CardModelParcelable?>("pelicula")

        val conneted = isNetworkVConnected(this)

        card?.let {

            val fmt = SimpleDateFormat("yyyy-MM-dd")
            val fmt2 = SimpleDateFormat("MMM yyyy")

            val date = fmt.parse(card.release_date ?: "")

            tvDescripcionEnDetalle.text = card.overview
            tvNombreEnDetalle.text = card.original_title
            tvRelease.text = fmt2.format(date ?: Date()).toUpperCase()
            tv_voteAverage.text = card.vote_average.toString()
            voteCount.text = card.vote_count.toString() + getString(R.string.votes)

            card.backdrop_path.let {

                val appDirectoryName = "RappiDemo"
                val imageRoot = File(
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                    ), appDirectoryName
                )

                val uriFile = Uri.fromFile(File(imageRoot,it!!.substring(1)))

                val requestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_image_default)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

                Glide.with(this)
                    .load(uriFile)
                    .apply(requestOptions)
                    .into(ivImagenDetalle)
            }

            if(conneted) showNetworkMessage(conneted)
        }

    }

    override fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            snackBar = Snackbar.make(containerVistaDetalle, "", Snackbar.LENGTH_LONG)


            val layout = snackBar?.getView() as Snackbar.SnackbarLayout
            layout.setBackgroundColor(ContextCompat.getColor(layout.context, android.R.color.transparent))
            layout.setPadding(0, 0, 0, 0)

            val snackView = LayoutInflater.from(this@VistaDetalleActivity).inflate(R.layout.snack_bar, null) as View

            layout.addView(snackView, 0)
            snackBar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackBar?.show()

        } else {
            snackBar?.dismiss()
        }
    }
}
