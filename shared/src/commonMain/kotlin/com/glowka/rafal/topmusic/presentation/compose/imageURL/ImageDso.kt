package com.glowka.rafal.topmusic.presentation.compose.imageURL

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ImageDso : RealmObject {
  @PrimaryKey
  var url: String? = null
  var picture: ByteArray? = null
  var accessDate: RealmInstant? = null
}
