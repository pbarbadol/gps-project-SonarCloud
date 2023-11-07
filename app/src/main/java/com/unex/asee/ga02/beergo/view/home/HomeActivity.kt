package com.unex.asee.ga02.beergo.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.unex.asee.ga02.beergo.R
import com.unex.asee.ga02.beergo.databinding.ActivityHomeBinding
import com.unex.asee.ga02.beergo.model.User

class HomeActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding //Creamos el binding
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    companion object {
        const val LOGIN_USER = "LOGIN_USER"

        public fun start(
            context: Context,
            user: User
        ) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(LOGIN_USER, user)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //Creamos la actividad
        binding =
            ActivityHomeBinding.inflate(layoutInflater) //Le decimos que el layout que va a usar es el activity_home.xml
        setContentView(binding.root) //Le decimos que el layout que va a usar es el activity_home.xml

        setUpUI() //Llamamos a la función que inicializa la interfaz de usuario
        setUpListeners() //Llamamos a la función que inicializa los listeners
    }


    fun setUpUI() {
        binding.bottomNavigationView.setupWithNavController(navController) //Le decimos que el navController que va a usar es el del navHostFragment
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.HomeFragment,
                R.id.LoginFragment,
            )
        )
        setSupportActionBar (binding.toolbar)
        setupActionBarWithNavController (navController, appBarConfiguration)
    }


override fun onSupportNavigateUp(): Boolean {
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
}

fun setUpListeners() {

    //TODO Aquí pondremos listener en caso de ser necesario
}

}