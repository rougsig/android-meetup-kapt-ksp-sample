package com.github.rougsig.kspsample.processor

import android.content.Context
import android.content.Intent
import com.github.rougsig.kspsample.processor.extensions.beginWithUpperCase
import com.github.rougsig.kspsample.processor.extensions.toSnakeCase
import com.squareup.kotlinpoet.*

internal fun generateActivityIntentFactory(params: ActivityIntentFactoryGeneratorParams): FileSpec {
  return FileSpec
    .builder(params.packageName, params.factoryName)
    .addType(TypeSpec
      .objectBuilder(params.factoryName)
      .addProperties(params.parameters.map { param ->
        PropertySpec
          .builder(param.name, String::class.asClassName())
          .initializer("%S", "EXTRA_${param.name.beginWithUpperCase().toSnakeCase()}")
          .addModifiers(KModifier.CONST)
          .build()
      })
      .addFunction(FunSpec
        .builder(params.methodName)
        .addParameter("context", Context::class.asClassName())
        .addParameters(params.parameters.map { param ->
          ParameterSpec
            .builder(param.name, param.type)
            .build()
        })
        .addCode(CodeBlock.builder()
          .addStatement("val intent = %T(context, %T::class.java)", Intent::class.asClassName(), params.targetActivity)
          .apply {
            params.parameters.forEach { param ->
              addStatement("intent.putExtra(%L, %L)", param.name.beginWithUpperCase().toSnakeCase(), param.name)
            }
          }
          .addStatement("return intent")
          .build())
        .returns(Intent::class.asClassName())
        .build())
      .build())
    .build()
}
