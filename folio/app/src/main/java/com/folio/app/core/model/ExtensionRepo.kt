package com.folio.app.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ExtensionRepo(
    val url: String,
    val addedAt: Long = System.currentTimeMillis(),
)

@Serializable
data class ExtensionIndex(
    val sources: List<ExtensionEntry> = emptyList(),
)

@Serializable
data class ExtensionEntry(
    val name: String = "",
    val pkg: String = "",
    val lang: String = "",
    val version: String = "",
    val nsfw: Int = 0,
    val sources: List<ExtensionSourceItem> = emptyList(),
)

@Serializable
data class ExtensionSourceItem(
    val name: String = "",
    val lang: String = "",
    val id: String = "",
    @SerialName("baseUrl") val baseUrl: String = "",
)
