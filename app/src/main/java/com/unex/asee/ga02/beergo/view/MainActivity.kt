package com.unex.asee.ga02.beergo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.unex.asee.ga02.beergo.R
import com.unex.asee.ga02.beergo.view.IniciarSesion

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, IniciarSesion.newInstance("Supuesto 1", "Supuesto 2"))
                .commitNow()
        }
    }
}