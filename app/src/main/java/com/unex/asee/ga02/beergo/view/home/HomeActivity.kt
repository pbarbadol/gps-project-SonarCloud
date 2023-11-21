package com.unex.asee.ga02.beergo.view.home

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.unex.asee.ga02.beergo.R
import com.unex.asee.ga02.beergo.databinding.ActivityHomeBinding
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.User

class HomeActivity : AppCompatActivity(), ListFragment.OnShowClickListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding //Creamos el binding
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }
    private lateinit var userViewModel: UserViewModel

    companion object {
        const val LOGIN_USER = "LOGIN_USER"
        val user = null

        public fun start(
            context: Context, user: User
        ) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Creamos la actividad

        // Inflar el diseño usando DataBinding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración de la barra de acción
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Obtener el usuario desde la actividad anterior
        val user = intent.getSerializableExtra(LOGIN_USER) as User

        // Inicialización del ViewModel
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.setUser(user)

        // Inicialización de la interfaz de usuario
        setUpUI(user)

        // Inicialización de los listeners
        setUpListeners()
    }


    override fun onShowClick(beer: Beer) {
        val bundle = Bundle()
        bundle.putParcelable("user", user) //Pasamos en un bundle el user
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
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView =
            searchItem?.actionView as SearchView // Configure the search info and add any event listeners.
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        //TODO: Comentado porque no está terminado. Ver sesión 4 de ASEE Ejers 2 y 3

        R.id.action_settings -> { // User chooses the "Settings" item. Show the app settings UI.
            val action = ListFragmentDirections.actionHomeToSettingsFragment()
            navController.navigate(action)
            true
        }

        else -> {
            // The user's action isn't recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
    fun setUpListeners() {
    }
}
