package com.umesh.cafehaze.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteDto(
    @SerialName("item_id")
    val itemId: Int
)