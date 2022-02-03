package ru.angryrobot.barriertape.demo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.angryrobot.barriertape.demo.databinding.DemoFragmentBinding

class DemoFragment : BaseFragment() {

    private lateinit var binding: DemoFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        binding = DemoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}