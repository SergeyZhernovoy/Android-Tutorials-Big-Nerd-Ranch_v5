package com.learning.codapizza.model

import androidx.annotation.StringRes
import com.learning.codapizza.R

enum class PizzaSize(
    @get:StringRes val size: Int,
    val ratio: Float
) {
    Small(R.string.pizza_small_size, 0.5f),
    Medium(R.string.pizza_medium_size, 1f),
    Large(R.string.pizza_large_size, 2f),
    Extra(R.string.pizza_extra_size, 2.5f)
}