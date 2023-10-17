package com.example.min_project_it

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.min_project_it.Fragments.Fragment1
import com.example.min_project_it.Fragments.Fragment2
import com.example.min_project_it.Fragments.Fragment3
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
//        binding.logoutBtn.setOnClickListener {
//            firebaseAuth.signOut()
//            checkUser()
//        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    showHomeFragment()
                    true
                }
                R.id.profile -> {
                    showAccountFragment()
                    true
                }
                R.id.settings -> {
                    showSettingsFragment()
                    true
                }
                R.id.logout -> {
                    firebaseAuth.signOut()
                    checkUser()
                    true
                }
                else -> false
            }
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

    private fun showHomeFragment() {
        // Set the toolbar title
        val fragment = Fragment1()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.frameLayout.id, fragment, "HomeFragment")
        fragmentTransaction.commit()
    }

    // Function to show the AccountFragment
    private fun showAccountFragment() {
        // Set the toolbar title
        val fragment = Fragment2()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.frameLayout.id, fragment, "AccountFragment")
        fragmentTransaction.commit()
    }

    // Function to show the SettingsFragment
    private fun showSettingsFragment() {
        // Set the toolbar title
        val fragment = Fragment3()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.frameLayout.id, fragment, "SettingsFragment")
        fragmentTransaction.commit()
    }
}