package com.glowka.rafal.topmusic.data.dso

import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class AlbumDso : RealmObject {
  @PrimaryKey
  var id: String = ""
  var artistName: String = ""
  var name: String = ""
  var releaseDate: String = ""
  var artworkUrl100: String = ""
  var copyright: String = ""
  var url: String = ""
  var countryCode: String = ""
  var genres: RealmList<GenreDso> = emptyList<GenreDso>().toRealmList()
}
