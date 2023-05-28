package com.glowka.rafal.topmusic.data.database

import com.glowka.rafal.topmusic.data.dso.AlbumDso
import com.glowka.rafal.topmusic.data.dso.ConfigKeyDso
import com.glowka.rafal.topmusic.data.dso.GenreDso
import com.glowka.rafal.topmusic.presentation.compose.imageURL.ImageDso
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

fun initRealm(): Realm {
  val config = RealmConfiguration.create(setOf(AlbumDso::class, GenreDso::class, ConfigKeyDso::class, ImageDso::class))
  return Realm.open(config)
}