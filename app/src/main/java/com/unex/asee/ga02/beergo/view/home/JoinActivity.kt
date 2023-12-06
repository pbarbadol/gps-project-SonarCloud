package com.unex.asee.ga02.beergo.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.ActivityJoinBinding
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.repository.UserRepository
import com.unex.asee.ga02.beergo.utils.CredentialCheck
import kotlinx.coroutines.launch


class JoinActivity : AppCompatActivity() {

    private lateinit var db: BeerGoDatabase
    private lateinit var binding: ActivityJoinBinding

    //Repositorio
    private lateinit var userRepository: UserRepository

    companion object {

        const val USERNAME = "JOIN_USERNAME"
        const val PASS = "JOIN_PASS"
        fun start(
            context: Context, responseLauncher: ActivityResultLauncher<Intent>
        ) {
            val intent = Intent(context, JoinActivity::class.java)
            responseLauncher.launch(intent)


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicialización de la base de datos
        db = BeerGoDatabase.getInstance(applicationContext)!!
        userRepository = UserRepository.getInstance(db.userDao())

        //views initialization and listeners
        setUpUI()
        setUpListeners()
    }

    private fun setUpUI() {
        //get attributes from xml using binding
    }

    private fun setUpListeners() {
        with(binding) {
            btRegister.setOnClickListener {//botón register
                join()
            }
        }
    }

    private fun join() {
        with(binding) {
            val check = CredentialCheck.join(
                etUsername.text.toString(), etPassword.text.toString(), etRepassword.text.toString()
            )
            if (check.fail) {
                notifyInvalidCredentials(check.msg)
            } else {
                lifecycleScope.launch {
                    try {
                        val registeredUser = userRepository.registerUser(etUsername.text.toString(), etPassword.text.toString())

                        if (registeredUser != null) {
                            Toast.makeText(
                                this@JoinActivity, "Usuario creado", Toast.LENGTH_SHORT
                            ).show()
                            navigateBackWithResult(registeredUser)
                        } else {
                            notifyInvalidCredentials("El usuario ya existe")
                        }
                    } catch (e: Exception) {
                        notifyInvalidCredentials(e.message ?: "Error al registrar usuario")
                    }
                }
            }
        }
    }


    private fun navigateBackWithResult(user: User) {
        //Creación del intent
        val intent = Intent().apply {
            putExtra(USERNAME, user.name)
            putExtra(PASS, user.password)
        }

        //Establecer el resultado
        setResult(RESULT_OK, intent)
        finish() //Solo se produce cuando las activities se inicialicen para obtener un resultado de ellas. Muy pocas veces se usa
    }

    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}