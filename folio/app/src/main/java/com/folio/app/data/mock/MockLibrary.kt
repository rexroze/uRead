package com.folio.app.data.mock

import com.folio.app.core.model.Media
import com.folio.app.core.model.MediaOrigin
import com.folio.app.core.model.MediaType
import com.folio.app.core.model.Source

/**
 * Stand-in content so the UI is alive before real sources/import land. Covers
 * are left null on purpose — the cover component renders a typographic
 * placeholder, which is part of the paper aesthetic.
 *
 * Replace this object with a Room-backed repository when persistence arrives.
 */
object MockLibrary {

    val library: List<Media> = listOf(
        Media(
            id = "1",
            title = "The Left Hand of Darkness",
            author = "Ursula K. Le Guin",
            type = MediaType.EBOOK,
            coverUrl = null,
            progress = 0.42f,
            totalUnits = 304,
            currentUnit = 128,
            isDownloaded = true,
        ),
        Media(
            id = "2",
            title = "Project Hail Mary",
            author = "Andy Weir",
            type = MediaType.AUDIOBOOK,
            coverUrl = null,
            progress = 0.18f,
            totalUnits = 58_000,
            currentUnit = 10_440,
            origin = MediaOrigin.Online("audible", "Audible"),
        ),
        Media(
            id = "3",
            title = "Vinland Saga",
            author = "Makoto Yukimura",
            type = MediaType.MANGA,
            coverUrl = null,
            progress = 0.66f,
            totalUnits = 210,
            currentUnit = 139,
            origin = MediaOrigin.Online("mangadex", "MangaDex"),
            isDownloaded = true,
        ),
        Media(
            id = "4",
            title = "Solo Leveling",
            author = "Chugong",
            type = MediaType.MANHWA,
            coverUrl = null,
            progress = 0.91f,
            totalUnits = 179,
            currentUnit = 163,
            origin = MediaOrigin.Online("webtoon", "Webtoon"),
        ),
        Media(
            id = "5",
            title = "Piranesi",
            author = "Susanna Clarke",
            type = MediaType.EBOOK,
            coverUrl = null,
            progress = 0f,
            totalUnits = 245,
        ),
        Media(
            id = "6",
            title = "Berserk",
            author = "Kentaro Miura",
            type = MediaType.MANGA,
            coverUrl = null,
            progress = 0f,
            totalUnits = 364,
            origin = MediaOrigin.Online("mangadex", "MangaDex"),
        ),
        Media(
            id = "7",
            title = "Babel",
            author = "R. F. Kuang",
            type = MediaType.EBOOK,
            coverUrl = null,
            progress = 1f,
            totalUnits = 560,
            currentUnit = 560,
            isDownloaded = true,
        ),
        Media(
            id = "8",
            title = "The Sandman",
            author = "Neil Gaiman",
            type = MediaType.COMIC,
            coverUrl = null,
            progress = 0.05f,
            totalUnits = 75,
            currentUnit = 4,
        ),
    )

    /** Entries currently in progress, most-recent first — feeds the home strip. */
    val continueReading: List<Media> =
        library.filter { it.isStarted }.sortedByDescending { it.progress }

    val sources: List<Source> = listOf(
        Source("mangadex", "MangaDex", MediaType.MANGA, isInstalled = true),
        Source("webtoon", "Webtoon", MediaType.MANHWA, isInstalled = true),
        Source("gutenberg", "Project Gutenberg", MediaType.EBOOK, isInstalled = true),
        Source("librivox", "LibriVox", MediaType.AUDIOBOOK),
        Source("comick", "ComicK", MediaType.COMIC),
        Source("standardebooks", "Standard Ebooks", MediaType.EBOOK),
    )

    fun byId(id: String): Media? = library.firstOrNull { it.id == id }
}
