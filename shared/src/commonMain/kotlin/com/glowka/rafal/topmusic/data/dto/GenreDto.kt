package com.glowka.rafal.topmusic.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GenreDto(
  @SerialName("genreId")
  val id: String?,
  @SerialName("name")
  val name: String?,
)