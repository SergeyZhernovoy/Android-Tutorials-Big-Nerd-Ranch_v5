package com.learning.codapizza.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.learning.codapizza.R

enum class Topping(
    @field:StringRes val toppingName: Int,
    @field:DrawableRes val pizzaOverlayImage: Int
) {
    Basil(R.string.topping_basil, R.drawable.topping_basil),
    Mushroom(R.string.topping_mushroom,R.drawable.topping_mushroom),
    Olive(R.string.topping_olive,R.drawable.topping_olive),
    Peppers(R.string.topping_peppers,R.drawable.topping_peppers),
    Pepperoni(R.string.topping_pepperoni,R.drawable.topping_pepperoni),
    Pineapple(R.string.topping_pineapple,R.drawable.topping_pineapple)
}