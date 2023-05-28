package com.glowka.rafal.topmusic.flow.dashboard.country

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.glowka.rafal.topmusic.presentation.compose.getAppTypography
import com.glowka.rafal.topmusic.presentation.style.Colors
import com.glowka.rafal.topmusic.presentation.style.FontSize
import com.glowka.rafal.topmusic.presentation.style.Margin
// import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
internal fun CountryScreenDialog(
  viewState: CountryDialogViewModelToViewInterface.ViewState,
  onViewEvent: (CountryDialogViewModelToViewInterface.ViewEvents) -> Unit
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

    LazyColumn {
      itemsIndexed(viewState.items) { index, item ->
        Text(
          modifier = Modifier
            .fillMaxWidth()
            .padding(
              top = Margin.m4,
              bottom = Margin.m4,
              start = Margin.m4,
              end = Margin.m4,
            )
            .clickable {
              onViewEvent(
                CountryDialogViewModelToViewInterface.ViewEvents.PickCountry(
                  position = index
                )
              )
            },
          text = item,
          color = if (viewState.selectedIndex == index) Colors.blue else Colors.dark,
          fontWeight = FontWeight.Bold,
          fontSize = FontSize.title
        )
      }
    }
  }
}

// @Preview
// @Composable
// private fun CountryScreenDialogPreview() {
//  CountryScreenDialog(
//    viewState = CountryDialogViewModelToViewInterface.ViewState(
//      selectedIndex = 0,
//      items = Country.values().map { country ->
//        TextResource.of(country.nameResId())
//      }
//    ),
//    onViewEvent = {}
//  )
// }