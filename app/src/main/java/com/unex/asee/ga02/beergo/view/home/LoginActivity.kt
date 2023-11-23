package com.unex.asee.ga02.beergo.view.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.ActivityLoginBinding
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.utils.CredentialCheck
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(){
    private lateinit var db: BeerGoDatabase
    private lateinit var binding: ActivityLoginBinding
    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val name = result.data?.getStringExtra(JoinActivity.USERNAME).orEmpty() //La interrogación es una manera de comprobar componentes nulos en la activity
                val password = result.data?.getStringExtra(JoinActivity.PASS).orEmpty()

                with(binding) {
                    etUsername.setText(name)
                    etPassword.setText(password)
                }
                Toast.makeText(
                    this@LoginActivity,
                    "New user ($name/$password) created",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //view binding and set content view
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicialización de la base de datos
        db = BeerGoDatabase.getInstance(applicationContext)!!

        //views initialization and listeners
        setUpUI()
        setUpListeners()
    }

    private fun setUpUI() {
        //get attributes from xml using binding
    }

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

    private fun checkLogin(){
        val check = CredentialCheck.login(binding.etUsername.text.toString(),
            binding.etPassword.text.toString())
        if (!check.fail){
            lifecycleScope.launch{
                val user =
                    db?.userDao()?.findByName(binding.etUsername.text.toString())
                if (user != null) {
                    val check =
                        CredentialCheck.passwordOk(binding.etPassword.text.toString(),
                            user.password)
                    if (check.fail) notifyInvalidCredentials(check.msg)
                    else navigateToHomeActivity(user!!, check.msg)
                }
                else notifyInvalidCredentials("Invalid username")
            }
        }
        else notifyInvalidCredentials(check.msg)
    }

    private fun navigateToHomeActivity(user: User, msg: String) {
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, HomeActivity::class.java) //Crea un intent explícito para lanzar la actividad objetivo
        intent.putExtra(HomeActivity.LOGIN_USER, user)
        startActivity(intent) //Usa el método startActivity con el intent creado
        HomeActivity.start(this, user) //Usa el método start de la clase HomeActivity
    }

    private fun navigateToJoin() {
        JoinActivity.start(this, responseLauncher)
    }

    private fun navigateToWebsite() {
        val webIntent = Uri.parse("https://trakt.tv/").let { webpage ->
            Intent(Intent.ACTION_VIEW, webpage)
        }
        startActivity(webIntent)
    }

    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}