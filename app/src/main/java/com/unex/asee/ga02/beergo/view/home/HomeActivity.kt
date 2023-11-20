package com.unex.asee.ga02.beergo.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.unex.asee.ga02.beergo.R
import com.unex.asee.ga02.beergo.databinding.ActivityHomeBinding
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.User


/**
 * Solo un push por CU
 * En la rama develop tienen que meterse los requisitos.
 * Es la rama develop la que se entrega, hereda de la rama main
 */
 class HomeActivity : AppCompatActivity() , ListFragment.OnShowClickListener {
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
            //val intent = Intent(context, HomeActivity::class.java)
            //intent.putExtra(LOGIN_USER, user)
            //context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //Creamos la actividad
        binding =
            ActivityHomeBinding.inflate(layoutInflater) //Le decimos que el layout que va a usar es el activity_home.xml

        setContentView(binding.root) //Le decimos que el layout que va a usar es el activity_home.xml
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val user =
            intent.getSerializableExtra(LOGIN_USER) as User //Recogemos el usuario que nos ha pasado la actividad anterior
        setUpUI(user) //Llamamos a la función que inicializa la interfaz de usuario
        setUpListeners() //Llamamos a la función que inicializa los listeners
    }

    override fun onShowClick(beer: Beer) {

        navController.navigate(
            ListFragmentDirections.actionListFragmentToShowBeerFragment(
                beer
            )
        )
    }

    fun setUpUI(user: User) {

        binding.bottomNavigationView.setupWithNavController(navController) //Le decimos que el navController que va a usar es el del navHostFragment

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.listFragment,
                R.id.statisticsFragment,
                R.id.historyFragment,
                R.id.settingsFragment,
                R.id.achievementsFragment

            )
        )

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.showBeerFragment) {
                binding.toolbar.menu.clear()
                binding.bottomNavigationView.visibility = View.GONE

            } else {
                binding.bottomNavigationView.visibility = View.VISIBLE
                binding.toolbar.visibility = View.VISIBLE
            }
        }

    }

        override fun onSupportNavigateUp(): Boolean {
            return navController.navigateUp(appBarConfiguration)
                    || super.onSupportNavigateUp()
        }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.appbar_menu, menu)
            val searchItem = menu?.findItem(R.id.action_search)
            val searchView =
                searchItem?.actionView as SearchView // Configure the search info and add any event listeners.
            return super.onCreateOptionsMenu(menu)
        }

        override fun onOptionsItemSelected(item: MenuItem) = when
                                                                     (item.itemId) {
            //TODO: Comentado porque no está terminado. Ver sesión 4 de ASEE Ejers 2 y 3
//        R.id.action_settings -> { // User chooses the "Settings" item. Show the app settings UI.
//            val action =
//                ListFragmentDirections.actionHomeToSettingsFragment()
//            navController.navigate(action)
//            true
//        }
//            R.id.action_profile -> {
//        // User chooses the "Settings" item. Show the app settings UI.
//        Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show()
//            true
//
//        Toast.makeText(this, "Idioma", Toast.LENGTH_SHORT).show()
//            true
//
//        Toast.makeText(this, "Cerrar sesión", Toast.LENGTH_SHORT).show()
//            true
//        }


            else -> {
                // The user's action isn't recognized.
                // Invoke the superclass to handle it.
                super.onOptionsItemSelected(item)
            }
        }

        fun setUpListeners() {
        }
    }





