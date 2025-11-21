package com.learning.codapizza.model

import androidx.annotation.StringRes
import com.learning.codapizza.R

enum class ToppingPlacement(
    @get:StringRes val label: Int
) {
    Left(R.string.placement_left),
    Right(R.string.placement_right),
    All(R.string.placement_all)
}