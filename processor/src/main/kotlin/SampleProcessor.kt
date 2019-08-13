package com.github.rougsig.sample.processor

import com.github.rougsig.sample.processor.base.Generator
import com.github.rougsig.sample.processor.sample.sampleGenerator
import com.github.rougsig.sample.runtime.SampleAnnotation
import com.google.auto.service.AutoService
import me.eugeniomarletti.kotlin.processing.KotlinAbstractProcessor
import java.io.File
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

private const val OPTION_GENERATED = "sample.generated"

@AutoService(Processor::class)
class SampleProcessor : KotlinAbstractProcessor() {
  private val sampleAnnotationClass = SampleAnnotation::class.java

  override fun getSupportedAnnotationTypes() = setOf(sampleAnnotationClass.canonicalName)

  override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

  override fun getSupportedOptions() = setOf(OPTION_GENERATED)

  override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
    for (type in roundEnv.getElementsAnnotatedWith(sampleAnnotationClass)) {
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
