package com.unex.asee.ga02.beergo.view.home
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.unex.asee.ga02.beergo.R
import com.unex.asee.ga02.beergo.databinding.ActivityHomeBinding
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.Comment
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.view.comment.CommentsFragment
import com.unex.asee.ga02.beergo.view.comment.CommentsFragmentDirections
import com.unex.asee.ga02.beergo.view.favs.FavsFragment
import com.unex.asee.ga02.beergo.view.list.ListFragment
import com.unex.asee.ga02.beergo.view.list.ListFragmentDirections
import com.unex.asee.ga02.beergo.view.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity(), ListFragment.OnShowClickListener , CommentsFragment.OnShowClickListener, FavsFragment.OnShowClickListener{
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding //Creamos el binding
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }
    private val viewModel: HomeViewModel by viewModels { HomeViewModel.Factory }
    companion object {
        const val LOGIN_USER = "LOGIN_USER"
        val user = null
        public fun start(
            context: Context,
            user: User
        ){}}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Creamos la actividad
        // Inflar el diseño usando DataBinding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Configuración de la barra de acción
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // Obtener el usuario desde la actividad anterior
        viewModel.userInSession = intent.getSerializableExtra(LOGIN_USER) as User //TODO: El usuario se obtiene del intent y se guarda en el HomeViewModel

        viewModel.setSelectedBeer(null)
        // Inicialización de la interfaz de usuario
        setUpUI()
        // Inicialización de los listeners
        setUpListeners()
    }
    override fun onShowClick(beer: Beer) {
        navController.navigate(
            ListFragmentDirections.actionListFragmentToShowBeerFragment()
        )
    }
    override fun onShowClick(comment: Comment) {
        navController.navigate(
            CommentsFragmentDirections.actionCommentsFragmentToAddCommentFragment()
        )
    }
    fun setUpUI() {
        binding.bottomNavigationView.setupWithNavController(navController) //Le decimos que el navController que va a usar es el del navHostFragment
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.listFragment,
                R.id.favsFragment,
                R.id.historyFragment,
                R.id.profileFragment,
                R.id.achievementsFragment

            )
        )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if ((destination.id == R.id.showBeerFragment) ||
                (destination.id == R.id.settingsFragment) || (destination.id == R.id.commentsFragment) ||
                (destination.id == R.id.addCommentFragment)
            ) {
                binding.toolbar.menu.findItem(R.id.action_settings).setVisible(false)
                binding.bottomNavigationView.visibility = View.GONE

            } else {
                binding.bottomNavigationView.visibility = View.VISIBLE
                binding.toolbar.visibility = View.VISIBLE
                val actionSettingsItem = binding.toolbar.menu?.findItem(R.id.action_settings)

                if (actionSettingsItem != null) {
                    actionSettingsItem.setVisible(true)
                }
            }
        }
    }
        override fun onSupportNavigateUp(): Boolean {
            return navController.navigateUp(appBarConfiguration)
                    || super.onSupportNavigateUp()
        }
        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.appbar_menu, menu)
            return super.onCreateOptionsMenu(menu)
        }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> { // User chooses the "Settings" item. Show the app settings UI.
            val action = ListFragmentDirections.actionHomeToSettingsFragment()
            navController.navigate(action)
            true
        }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
        fun setUpListeners() {
        }
    }