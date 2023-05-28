package com.glowka.rafal.topmusic.data.mapper

import com.glowka.rafal.topmusic.data.dso.AlbumDso
import com.glowka.rafal.topmusic.data.dso.GenreDso
import com.glowka.rafal.topmusic.domain.model.Album
import io.realm.kotlin.ext.toRealmList

interface AlbumToAlbumDsoMapper : Mapper<Album, AlbumDso>

class AlbumToAlbumDsoMapperImpl : AlbumToAlbumDsoMapper {

  override fun invoke(data: Album): AlbumDso {
    val genres = data.genres.map { genre ->
      GenreDso().apply {
        id = genre.id
        name = genre.name
      }
    }.toRealmList()
    return AlbumDso().apply {
      id = data.id
      name = data.name
      artistName = data.artistName
      releaseDate = data.releaseDate
      artworkUrl100 = data.artworkUrl100
      url = data.url
      copyright = data.copyright
      countryCode = data.countryCode
      this.genres = genres
    }
  }
}
