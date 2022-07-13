package com.github.rougsig.kspsample.runtime

import android.app.Activity
import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class ActivityParams(
  val activityClass: KClass<out Activity>
)
