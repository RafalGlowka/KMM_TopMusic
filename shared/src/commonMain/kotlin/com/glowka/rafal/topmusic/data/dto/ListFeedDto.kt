package com.glowka.rafal.topmusic.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListFeedDto(
  @SerialName("results")
  val results: List<AlbumDto?>?,
  @SerialName("copyright")
  val copyright: String?,
)
