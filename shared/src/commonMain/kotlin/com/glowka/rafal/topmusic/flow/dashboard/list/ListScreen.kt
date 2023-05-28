package com.glowka.rafal.topmusic.flow.dashboard.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.glowka.rafal.topmusic.MainRes
import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.model.countryName
import com.glowka.rafal.topmusic.presentation.compose.SnackbarEvent
import com.glowka.rafal.topmusic.presentation.compose.getAppTypography
import com.glowka.rafal.topmusic.presentation.compose.imageURL.ImageURL
import com.glowka.rafal.topmusic.presentation.compose.statusBarsPadding
import com.glowka.rafal.topmusic.presentation.compose.systemBarsPadding
import com.glowka.rafal.topmusic.presentation.style.Colors
import com.glowka.rafal.topmusic.presentation.style.FontSize
import com.glowka.rafal.topmusic.presentation.style.Margin
import io.github.skeptick.libres.compose.painterResource

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ListScreen(
  viewState: ListViewModelToViewInterface.ViewState,
  onViewEvent: (ListViewModelToViewInterface.ViewEvents) -> Unit
) {
//  val systemUiController = rememberSystemUiController()
//  val useDarkIcons = MaterialTheme.colors.isLight

  MaterialTheme(
    typography = getAppTypography()
  ) {
//    systemUiController.setSystemBarsColor(
//      color = Color.Transparent,
//      darkIcons = useDarkIcons
//    )
    val pullRefreshState = rememberPullRefreshState(
      viewState.isRefreshing,
      { onViewEvent(ListViewModelToViewInterface.ViewEvents.RefreshList) }
    )
    Box(
      modifier = Modifier
        .fillMaxSize()
        .pullRefresh(pullRefreshState)
        .background(Colors.white)
    ) {
      val items = viewState.items
      val gridState = rememberLazyGridState()
      if (items.isNotEmpty()) {
        val smallHeader = (gridState.firstVisibleItemIndex != 0)
        Box(modifier = Modifier.fillMaxSize()) {
          LazyVerticalGrid(
            modifier = Modifier.padding(10.dp),
            columns = GridCells.Fixed(count = 2),
            state = gridState,
          ) {
            header {
              Text(
                modifier = Modifier
                  .padding(
                    top = Margin.m4,
                    bottom = Margin.m4,
                    start = Margin.m4,
                    end = Margin.m4,
                  )
                  .clickable {
                    onViewEvent(ListViewModelToViewInterface.ViewEvents.PickCountry)
                  }
                  .statusBarsPadding(),
                text = MainRes.string.list_title.format(viewState.country.countryName),
                color = Colors.dark,
                fontWeight = FontWeight.Bold,
                fontSize = FontSize.title
              ) // or any composable for your single row
            }
            items(
              items.size,
              null
            ) {
              val album = items[it]
              AlbumListItem(
                album = album,
                onAlbumClick = {
                  onViewEvent(ListViewModelToViewInterface.ViewEvents.PickedAlbum(album))
                }
              )
            }
          }
          if (smallHeader) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .background(Colors.white_transparent)
                .statusBarsPadding()
            ) {
              Text(
                modifier = Modifier
                  .padding(
                    top = Margin.m3,
                    bottom = Margin.m3,
                    start = Margin.m3,
                    end = Margin.m3,
                  )
                  .clickable {
                    onViewEvent(ListViewModelToViewInterface.ViewEvents.PickCountry)
                  }
                  .fillMaxWidth(),
                text = MainRes.string.list_title.format(viewState.country.countryName),
                color = Colors.dark,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = FontSize.base,
              )
            }
          }
        }
      } else {
        Text(
          modifier = Modifier
            .fillMaxWidth(),
          textAlign = TextAlign.Center,
          text = viewState.errorMessage,
        )
      }

      PullRefreshIndicator(
        refreshing = viewState.isRefreshing,
        state = pullRefreshState,
        modifier = Modifier.align(Alignment.TopCenter).systemBarsPadding()
      )

      SnackbarEvent(
        modifier = Modifier.align(Alignment.BottomCenter),
        snakbarEvent = viewState.snackbarEvent,
        onViewEvent = onViewEvent,
      )
    }
  }
}

@Composable
private fun AlbumListItem(
  album: Album,
  onAlbumClick: () -> Unit
) {
  Card(
    Modifier
      .clickable { onAlbumClick() }
      .fillMaxWidth()
      .aspectRatio(1f)
      .padding(6.dp),
    shape = RoundedCornerShape(20.dp),
    elevation = 5.dp
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
    ) {
      ImageURL(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(1f),
        url = album.artworkUrl100,
        useMemoryCache = true,
        useDBCache = true,
        contentScale = ContentScale.Crop,
        contentDescription = "${album.artistName} ${album.name} cover",
        loading = {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .aspectRatio(1f),
            contentAlignment = Alignment.Center
          ) {
            CircularProgressIndicator(
              modifier = Modifier
                .fillMaxWidth(fraction = 0.5f)
                .aspectRatio(1f)
            )
          }
        },
        failure = {
          Image(
            painterResource(MainRes.image.ic_loading_error),
            contentDescription = MainRes.string.content_description_loading_image_error,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        }
      )
//      GlideImage(
//        imageModel = { album.artworkUrl100 },
//        loading = {
//          Box(
//            modifier = Modifier
//              .fillMaxWidth()
//              .aspectRatio(1f),
//            contentAlignment = Alignment.Center
//          ) {
//            CircularProgressIndicator(
//              modifier = Modifier
//                .fillMaxWidth(fraction = 0.5f)
//                .aspectRatio(1f)
//            )
//          }
//        },
//        failure = {
//          Image(
//            painterResource(R.drawable.music_error),
//            contentDescription = stringResource(id = R.string.content_description_loading_image_error),
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize()
//          )
//        }
//      )
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Colors.black_0,
                Colors.black_75,
              ),
            )
          )
      ) {}
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(Margin.m3),
        verticalArrangement = Arrangement.Bottom
//          contentAlignment = Alignment.BottomStart

      ) {
        Text(
          modifier = Modifier.wrapContentSize(),
          textAlign = TextAlign.Left,
          text = album.name,
          color = Color.White,
          fontWeight = FontWeight.SemiBold,
          fontSize = FontSize.base
        )
        Text(
          modifier = Modifier.wrapContentSize(),
          textAlign = TextAlign.Left,
          text = album.artistName,
          color = Color.Gray,
          fontWeight = FontWeight.Medium,
          fontSize = FontSize.small
        )
      }
    }
  }
}

private fun LazyGridScope.header(
  content: @Composable LazyGridItemScope.() -> Unit
) {
  item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}

// @Preview
// @Composable
// private fun AlbumListItemPreview() {
//  AlbumListItem(
//    album = Album(
//      id = "123",
//      name = "Super Jazz",
//      artistName = "Artist",
//      releaseDate = "2022.03.18",
//      artworkUrl100 = "www.wp.pl",
//      genres = persistentListOf(Genre(id = "123", name = "Jazz")),
//      copyright = "some copyright",
//      url = "www.wp.pl",
//      countryCode = "us",
//    ),
//    onAlbumClick = {}
//  )
// }
//
// @Preview
// @Composable
// private fun ListScreenPreview() {
//   ListScreen(
//     viewState = ListViewModelToViewInterface.ViewState(),
//     onViewEvent = {}
//   )
// }
