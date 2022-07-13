@file:OptIn(KspExperimental::class)

package com.github.rougsig.kspsample.processor

import com.github.rougsig.kspsample.runtime.ActivityParams
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.visitor.KSEmptyVisitor
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

class ActivityParamsProcessor(
  private val codeGenerator: CodeGenerator,
) : SymbolProcessor {
  private val activityParamsAnnotationClass = ActivityParams::class

  override fun process(resolver: Resolver): List<KSAnnotated> {
    val symbols = resolver
      .getSymbolsWithAnnotation(activityParamsAnnotationClass.qualifiedName!!)
      .filterIsInstance<KSClassDeclaration>()

    symbols.forEach { symbol ->
      val params = symbol.accept(Visitor(), Unit)
      val fileSpec = generateActivityIntentFactory(params)
      fileSpec.writeTo(codeGenerator, aggregating = false)
    }

    // One round processor. No extra rounds. No dependencies.
    return emptyList()
  }

  class Visitor : KSEmptyVisitor<Unit, ActivityIntentFactoryGeneratorParams>() {
    override fun visitClassDeclaration(
      classDeclaration: KSClassDeclaration,
      data: Unit,
    ): ActivityIntentFactoryGeneratorParams {
      val packageName = classDeclaration.packageName.asString()
      val methodName = "createIntent"

      val annotation = classDeclaration.getAnnotationsByType(ActivityParams::class).first()
      val targetActivity = annotation.activityClass.asClassName()
      val factoryName = "${targetActivity.simpleName}IntentFactory"

      val parameters = classDeclaration.primaryConstructor!!.parameters
        .map { parameter ->
          ActivityIntentFactoryGeneratorParams.Parameter(
            name = parameter.name!!.asString(),
            type = parameter.type.toTypeName()
          )
        }

      return ActivityIntentFactoryGeneratorParams(
        targetActivity = targetActivity,
        factoryName = factoryName,
        packageName = packageName,
        methodName = methodName,
        parameters = parameters
      )
    }

    override fun defaultHandler(node: KSNode, data: Unit): ActivityIntentFactoryGeneratorParams {
      error("should be never called. node='$node'")
    }
  }
}
