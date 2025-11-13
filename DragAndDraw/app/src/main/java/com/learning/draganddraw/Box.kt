package com.learning.draganddraw

import android.graphics.PointF
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Box(val start: PointF): Parcelable {

    var end: PointF = PointF(start.x, start.y)

    val left: Float
        get() = start.x.coerceAtMost(end.x)

    val right: Float
        get() = start.x.coerceAtLeast(end.x)

    val top: Float
        get() = start.y.coerceAtMost(end.y)

    val bottom: Float
        get() = start.y.coerceAtLeast(end.y)

}