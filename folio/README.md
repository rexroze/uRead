# Folio

A minimal, paper-like, all-in-one reading app for Android — ebooks, audiobooks,
manga, manhwa, comics and more, in one quiet library.

Folio takes inspiration from [uRead](https://github.com/rexroze/uread) but trades
the Tachiyomi/Mihon-style density for calm: warm paper tones, serif titles, a
two-colour palette, and chrome that gets out of the way of the words.

> Status: **early scaffold.** The design system, navigation and screens are real
> and run on mock data. Real sources, local-file import and the format engines
> (Readium / image pager / ExoPlayer) are stubbed at clearly marked seams.

## What's here

| Area | State |
| --- | --- |
| Design system (paper/ink theme, type scale, spacing, shapes) | ✅ Built |
| Navigation (Home · Library · Browse + Details + Reader) | ✅ Built |
| Home feed (Continue / Up next) | ✅ On mock data |
| Unified Library (filter chips, adaptive grid) | ✅ On mock data |
| Browse / sources surface | ✅ On mock data |
| Detail screen | ✅ On mock data |
| Immersive Reader shell (text + audio player) | ✅ Skeleton |
| Real online sources / extensions | ⛳ Stubbed |
| Local file import (EPUB / CBZ / M4B …) | ⛳ Stubbed |
| Persistence (Room) & sync | ⛳ Wired in deps, not implemented |
| Offline downloads, TTS | ⛳ Planned |

## Design principles

1. **The content is the colour.** Two tones — warm *paper* and soft *ink* — plus
   one restrained accent. No gradients, no loud chrome.
2. **Serif for titles, sans for UI.** Titles read as "book"; labels stay quiet.
3. **One library, many formats.** A single `Media` model unifies every type;
   adding a format is one enum entry plus a reader, not a new screen tree.
4. **Chrome on demand.** The reader is full-bleed by default; tap to summon
   controls, tap to dismiss.

## Architecture

```
com.folio.app
├─ core
│  ├─ designsystem   theme tokens + reusable components (cover, progress, header)
│  └─ model          Media, MediaType, Source — the unified content contract
├─ data
│  └─ mock           stand-in library; swap for a Room-backed repository
├─ feature
│  ├─ home  ├─ library  ├─ browse  ├─ details  └─ reader
└─ navigation         tabs, bottom bar, nav graph
```

Built with Jetpack Compose, Material 3, Navigation-Compose, Hilt, Room, DataStore,
Coil and Media3 — the same proven stack uRead uses.

## Build

Open in Android Studio (Ladybug+) and run the `app` configuration, or:

```bash
./gradlew :app:assembleDebug
```

Requires JDK 17 and the Android SDK (compileSdk 35, minSdk 28).

## Roadmap (next seams to fill)

- [ ] Room entities + repository replacing `MockLibrary`
- [ ] Local import: EPUB & CBZ parsers → `Media`
- [ ] Readium navigator in the Reader text body
- [ ] Image pager for manga/manhwa with reading-direction support
- [ ] ExoPlayer-backed audiobook service + media notification
- [ ] Pluggable online sources (extension registry)
- [ ] Reader themes (sepia / night / font) via DataStore
- [ ] Cross-device progress sync
