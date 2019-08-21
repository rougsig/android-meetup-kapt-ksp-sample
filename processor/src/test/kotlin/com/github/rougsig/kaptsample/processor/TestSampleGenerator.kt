package com.github.rougsig.kaptsample.processor

class TestSampleGenerator : APTest("com.github.rougsig.kaptsample.testmodels") {
  fun testSample() {
    testProcessor(AnnotationProcessor(
      sourceFiles = listOf("CatDetails.java"),
      destinationFile = "CatDetailsActivityIntentFactory.kt.txt",
      processor = ActivityParamsProcessor()
    ))
  }
}
