package com.glowka.rafal.topmusic.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumDto(
  @SerialName("id")
  val id: String?,
  @SerialName("artistName")
  val artistName: String?,
  @SerialName("name")
  val name: String?,
  @SerialName("releaseDate")
  val releaseDate: String?,
  @SerialName("artworkUrl100")
  val artworkUrl100: String?,
  @SerialName("genres")
  val genres: List<GenreDto>?,
  @SerialName("url")
  val url: String?,
)