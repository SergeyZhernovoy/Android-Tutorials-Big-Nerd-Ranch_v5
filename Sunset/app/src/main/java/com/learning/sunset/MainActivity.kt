package com.learning.sunset

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.learning.sunset.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val blueSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.blue_sky)
    }
    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.sunset_sky)
    }
    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.night_sky)
    }

    private var sunset = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scene.setOnClickListener {
            if(!sunset) {
                startAnimation()
            }else {
                reverseAnimation()
            }
        }

    }

    private fun startAnimation() {
        val sunYStart = binding.sun.top.toFloat()
        val sunYEnd = binding.sun.height.toFloat()

        val heightAnimator = ObjectAnimator
            .ofFloat(binding.sun, "y", sunYStart, sunYEnd)
            .setDuration(3000)

        heightAnimator.interpolator = AccelerateInterpolator()

        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", blueSkyColor, sunsetSkyColor)
            .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", sunsetSkyColor, nightSkyColor)
            .setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        val sunYHeatStart = 0f
        val sunYHeatEnd = 360f

        val heatAnimator = ObjectAnimator
            .ofFloat(binding.sun, "rotation", sunYHeatStart, sunYHeatEnd)
            .setDuration(9000)

        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
            .with(sunsetSkyAnimator)
            .with(heatAnimator)
            .before(nightSkyAnimator)
        animatorSet.start()
        sunset = true
//
//        heightAnimator.start()
//        sunsetSkyAnimator.start()
    }

    fun reverseAnimation() {
        val sunYStart = binding.sky.height.toFloat()
        val sunYEnd = binding.sun.top.toFloat()

        val heightAnimator = ObjectAnimator
            .ofFloat(binding.sun, "y", sunYStart, sunYEnd)
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val nightSkyAnimator = ObjectAnimator
            .ofInt(binding.sky,"backgroundColor", blueSkyColor, sunsetSkyColor)
            .setDuration(3000)

        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(binding.sky,"backgroundColor", sunsetSkyColor, blueSkyColor)
            .setDuration(3000)

        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
            .with(sunsetSkyAnimator)
            .before(nightSkyAnimator)

        animatorSet.start()

        sunset = false
    }

}