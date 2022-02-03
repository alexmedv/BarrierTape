package ru.angryrobot.barriertape.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.angryrobot.barriertape.demo.databinding.MainActivityBinding

import ru.angryrobot.barriertape.demo.fragments.AboutFragment
import ru.angryrobot.barriertape.demo.fragments.DemoFragment
import ru.angryrobot.barriertape.demo.fragments.PlaygroundFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportFragmentManager.fragments.isEmpty()) {
            supportFragmentManager.beginTransaction().replace(R.id.container, DemoFragment()).commit()
        }

        binding.navigationView.setOnItemSelectedListener {
            if (binding.navigationView.selectedItemId == it.itemId) return@setOnItemSelectedListener false
            when (it.itemId) {
                R.id.nav_demo -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, DemoFragment()).commit()
                    true
                }
                R.id.nav_playground -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, PlaygroundFragment()).commit()
                    true
                }
                R.id.nav_about -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, AboutFragment()).commit()
                    true
                }
                else -> false
            }
        }
    }
}