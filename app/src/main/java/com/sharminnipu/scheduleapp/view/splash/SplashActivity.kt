package com.sharminnipu.scheduleapp.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sharminnipu.scheduleapp.R
import com.sharminnipu.scheduleapp.databinding.ActivitySplashBinding
import com.sharminnipu.scheduleapp.view.home.HomeActivity

class SplashActivity : AppCompatActivity() {

    lateinit var binding:ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.getStartedBtn.setOnClickListener {
            finish()
            startActivity(Intent(this,HomeActivity::class.java))

        }
    }
}