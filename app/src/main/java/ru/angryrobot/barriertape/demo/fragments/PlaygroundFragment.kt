package ru.angryrobot.barriertape.demo.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.*
import com.google.android.material.radiobutton.MaterialRadioButton
import ru.angryrobot.barriertape.BarrierTapeDrawable
import ru.angryrobot.barriertape.Shape
import ru.angryrobot.barriertape.TriangleOrientation
import ru.angryrobot.barriertape.demo.R
import ru.angryrobot.barriertape.demo.databinding.PlaygroundFragmentBinding

class PlaygroundFragment : BaseFragment() , SeekBar.OnSeekBarChangeListener,
    CompoundButton.OnCheckedChangeListener {

    val barrierTape = BarrierTapeDrawable()

    private lateinit var binding: PlaygroundFragmentBinding
    //https://colorswall.com/
    private val palettes = listOf(
        listOf(Color.BLACK, Color.YELLOW),
        listOf(0x62bb47, 0xfcb827, 0xf6821f, 0xe03a3c, 0x963d97, 0x009ddc, 0xffffff, 0x999999, 0x000000),
        listOf( 0x673ab7, 0x5d34a5,  0x522e92, 0x482980, 0x3e236e, 0x341d5c, 0x291749, 0x1f1137, 0x150c25, 0x0a0612 ),
        listOf(0xff0000, 0xffa500, 0xffff00, 0x008000, 0x0000ff, 0x4b0082, 0xee82ee)
    )

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        binding = PlaygroundFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.seekBarX.setOnSeekBarChangeListener(this)
        binding.seekBarY.setOnSeekBarChangeListener(this)
        binding.radius.setOnSeekBarChangeListener(this)
        binding.width.setOnSeekBarChangeListener(this)

        binding.imageView.background = barrierTape

        repeat(palettes.size) { i ->
            binding.paletteGroup += MaterialRadioButton(requireActivity()).apply {
                isChecked = i == 0
                text = i.toString()
                tag = i
                setOnCheckedChangeListener(this@PlaygroundFragment)
            }
        }

        binding.reversed.setOnCheckedChangeListener { _, checkedId ->
            barrierTape.isReversed = checkedId
        }

        val shapes = Shape.values()
        binding.shapeType.apply {
            adapter = ArrayAdapter(requireActivity() , android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, shapes)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                    barrierTape.shape = shapes[position]
                    //configure blocks visibility
                    binding.orientationBlock.isVisible = barrierTape.shape == Shape.EQUILATERAL_TRIANGLE ||
                            barrierTape.shape == Shape.TRIANGLE
                    binding.radiusBlock.isVisible = barrierTape.shape == Shape.RECTANGLE

                }
                override fun onNothingSelected(parent: AdapterView<*>?) {  }
            }
        }

        val orientations = TriangleOrientation.values()
        binding.orientation.apply {
            adapter = ArrayAdapter(requireActivity() , android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, orientations)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                    barrierTape.triangleOrientation = orientations[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {  }
            }
        }

        binding.imageBlock.doOnPreDraw {
            with(binding) {
                seekBarY.max = imageBlock.height
                seekBarX.max = imageBlock.width
                seekBarY.progress = binding.imageView.height
                seekBarX.progress = binding.imageView.width
                width.max = imageBlock.width / 2
                radius.max = 150
            }
        }

    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (isChecked) {
            binding.paletteGroup.children.forEach {
                if (it != buttonView) (it as CompoundButton).isChecked = false
            }
            barrierTape.setColors(palettes[buttonView.tag as Int])
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (!fromUser) return

        when(seekBar.id) {

            R.id.seekBarX -> {
                binding.imageView.updateLayoutParams<FrameLayout.LayoutParams> {
                    width = progress
                }
            }

            R.id.seekBarY -> {
                binding.imageView.updateLayoutParams<FrameLayout.LayoutParams> {
                    height = progress
                }
            }

            R.id.radius -> barrierTape.setRadius(progress.toFloat())

            R.id.width -> barrierTape.lineWidth = progress + 1
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}