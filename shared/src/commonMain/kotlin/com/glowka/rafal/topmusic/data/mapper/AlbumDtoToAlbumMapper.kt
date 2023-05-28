package com.glowka.rafal.topmusic.data.mapper

import com.glowka.rafal.topmusic.data.dto.AlbumDto
import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.model.Genre
import com.glowka.rafal.topmusic.domain.utils.EMPTY
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class AlbumData(val album: AlbumDto, val copyright: String, val countryCode: String)

interface AlbumDtoToAlbumMapper : Mapper<AlbumData?, Album?>

class AlbumDtoToAlbumMapperImpl : AlbumDtoToAlbumMapper {

  override fun invoke(data: AlbumData?): Album? {
    data ?: return null
    data.album.id ?: return null
    data.album.name ?: return null
    data.album.artistName ?: return null
    data.album.releaseDate ?: return null
    data.album.artworkUrl100 ?: return null
    data.album.url ?: return null

    return Album(
      id = data.album.id,
      name = data.album.name,
      artistName = data.album.artistName,
      releaseDate = data.album.releaseDate,
      artworkUrl100 = data.album.artworkUrl100.replace("100x100", "512x512"),
      genres = data.album.genres?.map { genreDto ->
        Genre(genreDto.id ?: String.EMPTY, genreDto.name ?: String.EMPTY)
      }?.toImmutableList() ?: persistentListOf(),
      copyright = data.copyright,
      url = data.album.url,
      countryCode = data.countryCode,
    )
  }
}
