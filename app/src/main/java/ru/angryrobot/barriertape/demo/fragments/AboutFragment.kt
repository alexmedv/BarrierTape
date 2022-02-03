package ru.angryrobot.barriertape.demo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.angryrobot.barriertape.demo.databinding.AboutFragmentBinding

class AboutFragment : BaseFragment() {

    private lateinit var binding: AboutFragmentBinding

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        binding = AboutFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}