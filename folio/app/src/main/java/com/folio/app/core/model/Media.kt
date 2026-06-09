package com.folio.app.core.model

/**
 * A single library entry, regardless of format. One model to rule the home feed,
 * the library grid and the detail screen.
 *
 * @property progress 0f..1f — how far through the user is.
 * @property origin where this entry came from (a local import or an online source).
 */
data class Media(
    val id: String,
    val title: String,
    val author: String,
    val type: MediaType,
    val coverUrl: String?,
    val progress: Float = 0f,
    val totalUnits: Int = 0,      // pages, or seconds for audio
    val currentUnit: Int = 0,
    val origin: MediaOrigin = MediaOrigin.Local,
    val isDownloaded: Boolean = false,
    val accentSeed: Long = id.hashCode().toLong(),
) {
    val isStarted: Boolean get() = progress > 0f && progress < 1f
    val isFinished: Boolean get() = progress >= 1f
}

sealed interface MediaOrigin {
    /** Imported from the device. */
    data object Local : MediaOrigin

    /** Fetched from an online source/extension. */
    data class Online(val sourceId: String, val sourceName: String) : MediaOrigin
}
