package com.github.rougsig.kaptsample.processor.base

import com.squareup.kotlinpoet.FileSpec

internal interface Generator<T> {
  fun generateFile(type: T): FileSpec
}
