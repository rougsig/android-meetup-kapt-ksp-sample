package com.github.rougsig.kaptsample.processor.extensions

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import me.eugeniomarletti.kotlin.metadata.shadow.metadata.ProtoBuf
import me.eugeniomarletti.kotlin.metadata.shadow.metadata.deserialization.NameResolver
import me.eugeniomarletti.kotlin.metadata.shadow.name.FqName
import me.eugeniomarletti.kotlin.metadata.shadow.platform.JavaToKotlinClassMap

internal fun NameResolver.getClassName(index: Int): ClassName {
  return ClassName.bestGuess(getQualifiedClassName(index).replace("/", "."))
}

internal fun ProtoBuf.Type.asTypeName(nameResolver: NameResolver): TypeName {
  return nameResolver
    .getClassName(className)
    .let { className ->
      if (argumentList.isNotEmpty()) {
        val arguments = argumentList.map { it.type.asTypeName(nameResolver) }.toTypedArray()
        className.parameterizedBy(*arguments)
      } else {
        className
      }
    }
    .copy(nullable = nullable)
}
