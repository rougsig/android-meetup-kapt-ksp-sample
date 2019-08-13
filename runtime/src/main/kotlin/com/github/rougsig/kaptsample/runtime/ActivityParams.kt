package com.github.rougsig.kaptsample.runtime

import android.app.Activity
import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class ActivityParams(
  val activityClass: KClass<out Activity>
)
