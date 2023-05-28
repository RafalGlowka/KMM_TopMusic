package com.glowka.rafal.topmusic.domain.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

fun album(
  id: String = "1",
  artistName: String = "Jacek Mak",
  name: String = "album name",
  releaseDate: String = "1.01.1973",
  artworkUrl100: String = "https://artwork.com/album.jpg",
  genres: ImmutableList<Genre> = persistentListOf(),
  copyright: String = "Some copyright frazes",
  url: String = "https:/supermusic.com/album",
  countryCode: String = Country.UnitedStates.countryCode,
) = Album(
  id = id,
  artistName = artistName,
  name = name,
  releaseDate = releaseDate,
  artworkUrl100 = artworkUrl100,
  genres = genres,
  copyright = copyright,
  url = url,
  countryCode = countryCode,
)
