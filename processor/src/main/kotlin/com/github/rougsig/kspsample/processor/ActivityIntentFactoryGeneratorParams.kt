package com.github.rougsig.kspsample.processor

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName

data class ActivityIntentFactoryGeneratorParams(
  val targetActivity: ClassName,
  val factoryName: String,
  val packageName: String,
  val methodName: String,
  val parameters: List<Parameter>
) {
  data class Parameter(
    val name: String,
    val type: TypeName
  )
}
