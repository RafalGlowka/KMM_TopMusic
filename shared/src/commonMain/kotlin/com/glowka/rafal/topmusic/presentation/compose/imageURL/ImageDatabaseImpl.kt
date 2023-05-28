package com.glowka.rafal.topmusic.presentation.compose.imageURL

import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.types.RealmInstant

class ImageDatabaseImpl(
  private val realm: Realm,
) {
  suspend fun insertImage(image: ImageDso) {
    realm.write {
      copyToRealm(image, UpdatePolicy.ALL)
    }
  }

 suspend fun getImage(url: String): ImageDso? {
    return realm.query(ImageDso::class, "url == $0", url).first().find()
  }

  suspend fun deleteOlderThan(date: RealmInstant) {
    realm.write {
      val oldObjects = query(ImageDso::class, "accessDate < $0", date).find()
      delete(oldObjects)
    }
  }
}