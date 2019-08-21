package com.github.rougsig.kaptsample.testmodels

import android.app.Activity
import com.github.rougsig.kaptsample.runtime.ActivityParams

class CatDetailsActivity : Activity()

@ActivityParams(CatDetailsActivity::class)
data class CatDetails(val catId: String, val catName: String, val favoriteFish: String?)
