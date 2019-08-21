package com.github.rougsig.kaptsample.processor

import com.github.rougsig.kaptsample.runtime.ActivityParams
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FileSpec
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(ActivityParamsProcessor::class)
class ActivityParamsProcessor : AbstractProcessor() {
  private val generatedDir: File
    get() = processingEnv.options[KAPT_GENERATED_OPTION].let(::File)

  private val activityParamsAnnotationClass = ActivityParams::class.java

  override fun getSupportedAnnotationTypes(): Set<String> {
    return setOf(activityParamsAnnotationClass.canonicalName)
  }

  override fun getSupportedSourceVersion(): SourceVersion {
    return SourceVersion.latest()
  }

  override fun getSupportedOptions(): Set<String> {
    return setOf(KAPT_GENERATED_OPTION)
  }

  override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
    val elements = roundEnv.getElementsAnnotatedWith(activityParamsAnnotationClass)
    for (type in elements) {
      val targetElement = type as? TypeElement ?: continue
      val generatorParams = ActivityIntentFactoryGeneratorParams.create(targetElement)
      val fileSpec = generateActivityIntentFactory(generatorParams)
      fileSpec.writeToGeneratedDir()
    }
    return true
  }

  private fun FileSpec.writeToGeneratedDir() {
    val outputDirPath = "$generatedDir/${packageName.replace(".", "/")}"
    val outputDir = File(outputDirPath).also { it.mkdirs() }

    val file = File(outputDir, "$name.kt")
    file.writeText(toString())
  }
}

private const val KAPT_GENERATED_OPTION = "kapt.kotlin.generated"
