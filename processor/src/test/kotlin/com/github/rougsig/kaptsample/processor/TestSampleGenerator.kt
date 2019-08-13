package com.github.rougsig.kaptsample.processor

class TestSampleGenerator : APTest("com.github.rougsig.kaptsample.testmodels") {
  fun testSample() {
    testProcessor(AnnotationProcessor(
      sourceFiles = listOf("Sample.java"),
      destinationFile = "SampleGenerated.kt.txt",
      processor = SampleProcessor()
    ))
  }
}
