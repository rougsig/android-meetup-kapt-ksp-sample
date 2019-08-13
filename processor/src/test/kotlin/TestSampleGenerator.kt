package com.github.rougsig.mvifake.processor

import com.github.rougsig.sample.processor.SampleProcessor

class TestSampleGenerator : APTest("com.example.sample") {
  fun testSample() {
    testProcessor(AnnotationProcessor(
      sourceFiles = listOf("Sample.java"),
      destinationFile = "SampleGenerated.kt.txt",
      processor = SampleProcessor()
    ))
  }
}
