package com.glowka.rafal.topmusic.data.database

import co.touchlab.kermit.Logger
import com.glowka.rafal.topmusic.data.dso.AlbumDso
import com.glowka.rafal.topmusic.data.dso.ConfigKeyDso
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy

interface MusicDatabase {

  suspend fun insertAlbum(album: AlbumDso)

  suspend fun getAlbumsByCountryCode(countryCode: String): List<AlbumDso>

  suspend fun deleteAlbumsWithCountryCode(countryCode: String)
  suspend fun clearDatabase()

  suspend fun getConfigKey(key: String): String?
  suspend fun setConfigKey(key: String, value: String)
}

// Consider internal
class MusicDatabaseImpl(
  private val realm: Realm,
) : MusicDatabase {

  private val log = Logger.withTag("MusicDatabase")

  override suspend fun insertAlbum(album: AlbumDso) {
//    log.d("insert album")
    realm.write {
      copyToRealm(album, UpdatePolicy.ALL)
    }
  }

  override suspend fun getAlbumsByCountryCode(countryCode: String): List<AlbumDso> {
//    log.d("get albums")
    return realm.query(AlbumDso::class, "countryCode == $0", countryCode).find()
  }

  override suspend fun deleteAlbumsWithCountryCode(countryCode: String) {
//    log.d("delete albums")
    realm.write {
      val albumsWithCC = query(AlbumDso::class, "countryCode == $0", countryCode).find()
      delete(albumsWithCC)
    }
  }

  override suspend fun clearDatabase() {
    log.d("clear DB")
    realm.write {
      val allAlbums = query(AlbumDso::class).find()
      delete(allAlbums)
    }
  }

  override suspend fun getConfigKey(key: String): String? {
//    log.d("getKey $key")
    return try {
      realm.query(ConfigKeyDso::class, "key == $0", key).find().firstOrNull()?.value
    } catch (e: NullPointerException) {
      null
    }
  }

  override suspend fun setConfigKey(key: String, value: String) {
//    log.d("setKey $key")
    realm.write {
      val configKey = ConfigKeyDso().apply {
        this.key = key
        this.value = value
      }
      copyToRealm(configKey, UpdatePolicy.ALL)
    }
  }
}