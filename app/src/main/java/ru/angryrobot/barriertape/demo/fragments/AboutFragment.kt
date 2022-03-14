package ru.angryrobot.barriertape.demo.fragments

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.angryrobot.barriertape.demo.databinding.AboutFragmentBinding

class AboutFragment : BaseFragment() {

    val url = "https://github.com/alexmedv/BarrierTape"

    private lateinit var binding: AboutFragmentBinding

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        binding = AboutFragmentBinding.inflate(inflater, container, false)
        binding.githubLink.apply {
            text = url
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }

        binding.githubLinkBlock.setOnClickListener {
            requireActivity().startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) })
        }
        return binding.root
    }
}