package com.unex.asee.ga02.beergo.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import es.unex.giiis.asee.tiviclones02.databinding.ActivityHomeBinding
import es.unex.giiis.asee.tiviclones02.model.User

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    companion object { //Forma de poner valores estáticos en kotlin
        const val LOGIN_USER = "LOGIN_USER"

        public fun start(context: Context,
                         user: User){
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(LOGIN_USER, user)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        //Vinculación de vista
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getSerializableExtra(LOGIN_USER) as User
        binding.tvGreeting.text = "Hello ${user.name}"
    }
}