package com.glowka.rafal.topmusic.presentation.formatter

import com.glowka.rafal.topmusic.domain.utils.EMPTY
import com.glowka.rafal.topmusic.presentation.formatter.ReleaseDateFormatter
import com.glowka.rafal.topmusic.presentation.formatter.ReleaseDateFormatterImpl
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Exhaustive
import io.kotest.property.exhaustive.collection
import io.kotest.property.forAll

class ReleaseDateFormatterSpec : DescribeSpec() {

  init {
    val testSet = listOf(
      Pair("2000-12-31", "Released December 31, 2000"),
      Pair("2022-01-21", "Released January 21, 2022"),
      Pair("", String.EMPTY),
      Pair("incorrect data", String.EMPTY),
    )

    val formatter: ReleaseDateFormatter = ReleaseDateFormatterImpl()

    it("format dates correctly") {
//      Locale.setDefault(Locale("en", "EN"))
      Exhaustive.collection(testSet).forAll { (dataIn, expectedResult) ->
        formatter.format(dataIn) shouldBe expectedResult
        true
      }
    }
  }
}