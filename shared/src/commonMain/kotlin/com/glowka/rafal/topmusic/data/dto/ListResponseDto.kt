package com.glowka.rafal.topmusic.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListResponseDto(

  @SerialName("feed")
  val feed: ListFeedDto?
)
