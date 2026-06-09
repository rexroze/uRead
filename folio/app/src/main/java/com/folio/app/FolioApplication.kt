package com.folio.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * App entry point. Hilt's root — repositories, players and source registries get
 * wired in here as the app grows.
 */
@HiltAndroidApp
class FolioApplication : Application()
