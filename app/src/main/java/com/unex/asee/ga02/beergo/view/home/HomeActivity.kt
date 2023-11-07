package com.unex.asee.ga02.beergo.view.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.unex.asee.ga02.beergo.R
import com.unex.asee.ga02.beergo.databinding.ActivityHomeBinding
import com.unex.asee.ga02.beergo.model.User

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding //Creamos el binding
    //lateinit var ListFragment: ListFragment
    //lateinit var BeerFragment: BeerFragment
    //lateinit var SettingFragment: SettingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //Creamos la actividad
        binding = ActivityHomeBinding.inflate(layoutInflater) //Le decimos que el layout que va a usar es el activity_home.xml
        setContentView(binding.root) //Le decimos que el layout que va a usar es el activity_home.xml

        //val user = intent.getSerializableExtra(USER_INFO) as User //Obtenemos el usuario que nos pasan por parámetro

        setUpUI() //Llamamos a la función que inicializa la interfaz de usuario
        setUpListeners() //Llamamos a la función que inicializa los listeners
    }


    fun setUpUI() {
        //ListFragment = ListFragment() //Creamos los fragmentos
        //BeerFragment = BeerFragment()
        //SettingFragment = SettingFragment()

        val navHostFragment = //Creamos el navHostFragment
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment //Le decimos que el fragmento que va a usar es el nav_host_fragment
        binding.bottomNavigationView.setupWithNavController(navHostFragment.navController) //Le decimos que el navController que va a usar es el del navHostFragment
    }

    fun setUpListeners() {

        //TODO Aquí pondremos listener en caso de ser necesario
    }

}