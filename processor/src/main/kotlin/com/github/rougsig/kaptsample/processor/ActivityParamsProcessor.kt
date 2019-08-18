package com.github.rougsig.kaptsample.processor

import com.github.rougsig.kaptsample.processor.base.Generator
import com.github.rougsig.kaptsample.processor.sample.sampleGenerator
import com.github.rougsig.kaptsample.runtime.ActivityParams
import com.google.auto.service.AutoService
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
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
    for (type in roundEnv.getElementsAnnotatedWith(activityParamsAnnotationClass)) {
      sampleGenerator.generateAndWrite(Unit)
    }
    return true
  }

  private fun <T> Generator<T>.generateAndWrite(type: T) {
    val fileSpec = generateFile(type)

    val outputDirPath = "$generatedDir/${fileSpec.packageName.replace(".", "/")}"
    val outputDir = File(outputDirPath).also { it.mkdirs() }

    val file = File(outputDir, "${fileSpec.name}.kt")
    file.writeText(fileSpec.toString())
  }
}

private const val KAPT_GENERATED_OPTION = "kapt.kotlin.generated"
