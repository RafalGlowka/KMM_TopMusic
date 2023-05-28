package com.glowka.rafal.topmusic.domain.model

import kotlinx.collections.immutable.ImmutableList

data class Album(
  val id: String,
  val artistName: String,
  val name: String,
  val releaseDate: String,
  val artworkUrl100: String,
  val genres: ImmutableList<Genre>,
  val copyright: String,
  val url: String,
  val countryCode: String,
)
