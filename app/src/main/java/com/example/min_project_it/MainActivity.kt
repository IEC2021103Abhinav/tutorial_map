package com.example.min_project_it

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.min_project_it.databinding.ActivityMainBinding
import com.example.min_project_it.databinding.ActivityOtpBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()
        checkUser()
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }


    }

    private fun checkUser() {
        val firebaseUser=firebaseAuth.currentUser
        if(firebaseUser==null)
        {
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }
        else
        {
            val phone=firebaseUser.phoneNumber

        }
    }
}