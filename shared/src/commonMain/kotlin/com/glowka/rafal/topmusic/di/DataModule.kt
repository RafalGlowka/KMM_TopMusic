package com.glowka.rafal.topmusic.di

import com.glowka.rafal.topmusic.data.database.MusicDatabase
import com.glowka.rafal.topmusic.data.database.MusicDatabaseImpl
import com.glowka.rafal.topmusic.data.database.initRealm
import com.glowka.rafal.topmusic.presentation.compose.imageURL.ImageURLDBCache
import com.glowka.rafal.topmusic.presentation.compose.imageURL.ImageURLDBCacheImpl
import com.glowka.rafal.topmusic.presentation.compose.imageURL.ImageURLMemoryCache
import com.glowka.rafal.topmusic.presentation.compose.imageURL.ImageURLMemoryCacheImpl
import io.realm.kotlin.Realm
import org.koin.dsl.module
import kotlin.time.Duration.Companion.days

@Suppress("MagicNumber")
val dataModule = module {
  single<Realm> {
    initRealm()
  }

  single<MusicDatabase> {
    MusicDatabaseImpl(
      realm = get()
    )
  }

  single<ImageURLMemoryCache> {
    ImageURLMemoryCacheImpl(40)
  }

  single<ImageURLDBCache> {
    ImageURLDBCacheImpl(
      realm = get(),
      dataValidDuration = 30.days
    )
  }
}
