package com.github.rougsig.kaptsample.testmodels

import android.app.Activity
import com.github.rougsig.kaptsample.runtime.ActivityParams

@ActivityParams(activityClass = Activity::class)
data class CatDetails(
  val catId: String
)
