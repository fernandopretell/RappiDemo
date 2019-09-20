package com.fernandopretell.rappidemo.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fernandopretell.rappidemo.R
import com.fernandopretell.rappidemo.base.BaseActivity

class BienvenidaActivity : BaseActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)


    }

    override fun showNetworkMessage(isConnected: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
