package com.github.rougsig.kaptsample.processor

import com.github.rougsig.kaptsample.processor.extensions.asTypeName
import com.github.rougsig.kaptsample.processor.extensions.beginWithUpperCase
import com.github.rougsig.kaptsample.runtime.ActivityParams
import com.squareup.kotlinpoet.*
import javax.lang.model.element.TypeElement
import me.eugeniomarletti.kotlin.metadata.KotlinClassMetadata
import me.eugeniomarletti.kotlin.metadata.isPrimary
import me.eugeniomarletti.kotlin.metadata.kotlinMetadata
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.MirroredTypeException

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

  companion object {
    fun create(targetElement: TypeElement): ActivityIntentFactoryGeneratorParams {
      val className = targetElement.asClassName()
      val packageName = className.packageName
      val methodName = "createIntent"

      val annotation = targetElement.getAnnotation(ActivityParams::class.java)
      val targetActivity = try {
        annotation.activityClass.asClassName()
      } catch (mte: MirroredTypeException) {
        val classTypeMirror = mte.typeMirror as DeclaredType
        val classTypeElement = classTypeMirror.asElement() as TypeElement
        classTypeElement.asClassName()
      }
      val targetActivitySimpleName = targetActivity.simpleName
      val factoryName = "${targetActivitySimpleName}IntentFactory"

      val metadata = targetElement.kotlinMetadata as KotlinClassMetadata
      val proto = metadata.data.classProto
      val nameResolver = metadata.data.nameResolver
      val parameters = proto.constructorList
        .first { it.isPrimary }
        .valueParameterList
        .map { parameter ->
          Parameter(
            name = nameResolver.getString(parameter.name),
            type = parameter.type.asTypeName(nameResolver)
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
  }
}
