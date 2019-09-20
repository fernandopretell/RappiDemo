package com.fernandopretell.rappidemo.base

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fernandopretell.rappidemo.R
import com.fernandopretell.rappidemo.util.ConnectivityReceiver

abstract class BaseActivity(): AppCompatActivity(),ConnectivityReceiver.ConnectivityReceiverListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransitionEnter()

        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun finish() {
        super.finish()
        overridePendingTransitionExit()
    }

    protected fun unregisterNetworkChanges() {
        try {
            unregisterReceiver(ConnectivityReceiver())
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        unregisterNetworkChanges()
    }

    abstract fun showNetworkMessage(isConnected: Boolean)

    fun isNetworkVConnected(context: Context):Boolean{
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected

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