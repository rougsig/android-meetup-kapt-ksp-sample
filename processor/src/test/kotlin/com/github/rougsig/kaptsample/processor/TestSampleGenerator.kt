package com.github.rougsig.kaptsample.processor

class TestSampleGenerator : APTest("com.github.rougsig.kaptsample.testmodels") {
  fun testSample() {
    testProcessor(AnnotationProcessor(
      sourceFiles = listOf("CatDetails.java"),
      destinationFile = "SampleGenerated.kt.txt",
      processor = ActivityParamsProcessor()
    ))
  }
}
