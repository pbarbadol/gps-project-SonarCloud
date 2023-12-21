package com.unex.asee.ga02.beergo.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.ActivityJoinBinding
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.repository.UserRepository
import com.unex.asee.ga02.beergo.utils.CredentialCheck
import com.unex.asee.ga02.beergo.view.viewmodel.JoinViewModel
import kotlinx.coroutines.launch


class JoinActivity : AppCompatActivity() {


    private lateinit var binding: ActivityJoinBinding

    //ViewModel
    private val viewModel: JoinViewModel by viewModels { JoinViewModel.Factory  }

    companion object {

        const val USERNAME = "JOIN_USERNAME"
        const val PASS = "JOIN_PASS"
        fun start(
            context: Context, responseLauncher: ActivityResultLauncher<Intent>
        ) {
            responseLauncher.launch(Intent(context, JoinActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                        val registeredUser = viewModel.registerUser(etUsername.text.toString(), etPassword.text.toString())

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