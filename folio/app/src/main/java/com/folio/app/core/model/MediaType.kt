package com.folio.app.core.model

/**
 * Every kind of thing Folio can read or play. The UI is unified around this enum
 * so adding a new format later (e.g. PDF, light novels) is a single new entry
 * plus a reader/player, not a new screen tree.
 */
enum class MediaType(val label: String) {
    EBOOK("Ebook"),
    AUDIOBOOK("Audiobook"),
    MANGA("Manga"),
    MANHWA("Manhwa"),
    COMIC("Comic"),
    ;

    /** True when this type is consumed page-by-page as images. */
    val isPaged: Boolean
        get() = this == MANGA || this == MANHWA || this == COMIC

    /** True when this type is listened to rather than read. */
    val isAudio: Boolean
        get() = this == AUDIOBOOK
}
