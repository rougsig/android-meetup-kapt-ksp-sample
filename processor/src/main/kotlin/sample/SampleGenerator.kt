package com.github.rougsig.sample.processor.sample

import com.github.rougsig.sample.processor.base.Generator
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec

internal val sampleGenerator = SampleGenerator()

internal class SampleGenerator : Generator<Unit> {
  override fun generateFile(type: Unit): FileSpec {
    return FileSpec
        .builder("com.example.sample", "SampleGenerated")
        .addType(TypeSpec
            .classBuilder("SampleGenerated")
            .build())
        .build()
  }
}
