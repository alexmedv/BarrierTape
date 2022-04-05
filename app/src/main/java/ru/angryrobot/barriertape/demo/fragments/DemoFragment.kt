package ru.angryrobot.barriertape.demo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.angryrobot.barriertape.BarrierTapeDrawable
import ru.angryrobot.barriertape.Shape
import ru.angryrobot.barriertape.TriangleOrientation
import ru.angryrobot.barriertape.demo.R
import ru.angryrobot.barriertape.demo.databinding.DemoFragmentBinding

class DemoFragment : BaseFragment() {

    private lateinit var binding: DemoFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        binding = DemoFragmentBinding.inflate(inflater, container, false)

        binding.tape1.background = BarrierTapeDrawable().apply {
            stripeWidth = 20.dp
        }

        binding.tape2.background = BarrierTapeDrawable().apply {
            shape = Shape.CIRCLE
            stripeWidth = 20.dp
            borderWidth = 10.dp
        }

        binding.tape3.background = BarrierTapeDrawable().apply {
            shape = Shape.EQUILATERAL_TRIANGLE
            triangleOrientation = TriangleOrientation.TOP
            stripeWidth = 20.dp
            borderWidth = 10.dp
        }

        binding.tape4.background = BarrierTapeDrawable().apply {
            stripeWidth = 20.dp
        }

        binding.tape5.background = BarrierTapeDrawable().apply {
            stripeWidth = 20.dp
        }

        binding.tape6.background = BarrierTapeDrawable().apply {
            stripeWidth = 20.dp
            setColors(listOf(0xcf322e, 0xd7d4d5))
            setRadius(10F.dp)
            borderWidth = 10.dp
        }
        val laserColor = resources.getColor(R.color.laser_icon)
        binding.tape7.background = BarrierTapeDrawable().apply {
            stripeWidth = 20.dp
            isReversed = true
            setColors(listOf(laserColor, 0x000000))
            setRadius(20F.dp)
        }

        return binding.root
    }
}