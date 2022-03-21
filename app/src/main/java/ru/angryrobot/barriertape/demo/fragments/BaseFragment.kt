package ru.angryrobot.barriertape.demo.fragments

import android.os.Bundle

import androidx.fragment.app.Fragment
import androidx.transition.Fade
import androidx.transition.Visibility

open class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Fade(Visibility.MODE_IN)
        exitTransition = Fade(Visibility.MODE_OUT)
    }

    val Int.dp
    get() = (this * resources.displayMetrics.density).toInt()

}