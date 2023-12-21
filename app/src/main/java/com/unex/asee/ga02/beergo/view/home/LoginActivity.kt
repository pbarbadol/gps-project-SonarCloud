package com.unex.asee.ga02.beergo.view.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.ActivityLoginBinding
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.repository.UserRepository
import com.unex.asee.ga02.beergo.utils.CredentialCheck
import com.unex.asee.ga02.beergo.view.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

/**
 * Actividad que maneja la pantalla de inicio de sesión.
 */
class LoginActivity : AppCompatActivity(){
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels { LoginViewModel.Factory }
    // Resultado lanzador para la actividad de registro
    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val name = result.data?.getStringExtra(JoinActivity.USERNAME).orEmpty() //La interrogación es una manera de comprobar componentes nulos en la activity
                val password = result.data?.getStringExtra(JoinActivity.PASS).orEmpty()

                // Actualiza los campos de usuario y contraseña con los valores obtenidos
                with(binding) {
                    etUsername.setText(name)
                    etPassword.setText(password)
                }
                // Muestra un mensaje indicando que se ha creado un nuevo usuario
                Toast.makeText(
                    this@LoginActivity,
                    "New user ($name/$password) created",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuración del view binding y establecimiento del contenido de la vista
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Inicialización de las vistas y los listeners
        setUpUI()
        setUpListeners()
        // Lee las preferencias de la configuración
        readSetings()
    }
    /**
     * Lee las preferencias de configuración para recordar el nombre de usuario y la contraseña.
     */
    private fun readSetings(){
        val preferences = PreferenceManager.getDefaultSharedPreferences(this).all

        val rememberme = preferences["rememberme"] as Boolean? ?:false
        val username = preferences["username"] as String? ?: ""
        val passwd = preferences["password"] as String? ?: ""

        // Si se recuerda al usuario, establece los campos de usuario y contraseña
        if (rememberme){
            binding.etUsername.setText(username)
            binding.etPassword.setText(passwd)
        }
    }
    /**
     * Inicializa las vistas utilizando el view binding.
     */
    private fun setUpUI() {}

    /**
     * Configura los listeners para los botones.
     */
    private fun setUpListeners() {
        with(binding) {

            btLogin.setOnClickListener {
                checkLogin()
            }

            btRegister.setOnClickListener {
                navigateToJoin()
            }

        }
    }

    /**
     * Realiza la validación del inicio de sesión.
     */
    private fun checkLogin() {
        val check = CredentialCheck.login(binding.etUsername.text.toString(), binding.etPassword.text.toString())
        if (!check.fail) {
            lifecycleScope.launch {
                try {
                    // Intenta autenticar al usuario utilizando el UserRepository
                    val user = viewModel.loginUser(binding.etUsername.text.toString(), binding.etPassword.text.toString())
                    navigateToHomeActivity(user, "Login successful")
                } catch (e: Exception) {
                    // Maneja excepciones relacionadas con la autenticación
                    notifyInvalidCredentials(e.message ?: "Authentication failed")
                }
            }
        } else {
            // Notifica si hay un problema con las credenciales
            notifyInvalidCredentials(check.msg)
        }
    }

    /**
     * Navega a la actividad principal después de un inicio de sesión exitoso.
     */
    private fun navigateToHomeActivity(user: User, msg: String) {
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, HomeActivity::class.java) //Crea un intent explícito para lanzar la actividad objetivo
        intent.putExtra(HomeActivity.LOGIN_USER, user)
        startActivity(intent) //Usa el método startActivity con el intent creado
        HomeActivity.start(this, user) //Usa el método start de la clase HomeActivity
    }

    /**
     * Navega a la actividad de registro.
     */
    private fun navigateToJoin() {
        JoinActivity.start(this, responseLauncher)
    }

    /**
     * Muestra un mensaje de error al usuario.
     */
    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}