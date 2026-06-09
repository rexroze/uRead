package com.folio.app.core.model

/**
 * An online content source (the "extension" concept, à la Tachiyomi). Kept as a
 * plain model here; a real build would load these from installable plugins.
 */
data class Source(
    val id: String,
    val name: String,
    val supports: MediaType,
    val language: String = "en",
    val isInstalled: Boolean = false,
)
