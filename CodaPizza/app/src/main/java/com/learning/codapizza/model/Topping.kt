package com.learning.codapizza.model

import androidx.annotation.StringRes
import com.learning.codapizza.R

enum class Topping(
    @field:StringRes val toppingName: Int
) {
    Basil(R.string.topping_basil),
    Mushroom(R.string.topping_mushroom),
    Olive(R.string.topping_olive),
    Peppers(R.string.topping_peppers),
    Pepperoni(R.string.topping_pepperoni),
    Pineapple(R.string.topping_pineapple)
}