package com.russia.criminalintent.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Crime(
    @PrimaryKey
    val id: UUID,
    var title: String,
    var date: Date,
    var isSolved: Boolean = false,
    var requiresPolice: Boolean = false,
    var suspect: String = "",
    var phone: String = "",
    val photoFileName: String? = null
)
