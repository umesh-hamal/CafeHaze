package com.umesh.cafehaze.model.data

import kotlinx.serialization.Serializable
@Serializable
data class SalesPoint(
    val label: String,
    val total: Double // ✅ not Float
)