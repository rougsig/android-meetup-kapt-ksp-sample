package com.github.rougsig.kspsample.processor

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.intellij.lang.annotations.Language
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import kotlin.test.assertEquals

class ActivityParamsProcessorTest {

  @Rule
  @JvmField
  var temporaryFolder: TemporaryFolder = TemporaryFolder()

  @Test
  fun `should generate CatDetailsActivityIntentFactory`() {
    val kotlinSource = SourceFile.kotlin(
      name = "CatDetailsActivity.kt",
      contents = """
        |package com.github.rougsig.kspsample.test
        |
        |import com.github.rougsig.kspsample.runtime.ActivityParams
        |
        |@ActivityParams(CatDetailsActivity::class)
        |data class CatDetails(val catId: String, val catName: String, val favoriteFish: String?)
        |      
      """.trimMargin()
    )

    val compilationResult = compile(kotlinSource)
    println(compilationResult.messages)

    assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
    assertSourceEquals(
      expected = """
        |package com.github.rougsig.kspsample.test
        |
        |import android.content.Context
        |import android.content.Intent
        |import kotlin.String
        |
        |public object CatDetailsActivityIntentFactory {
        |  public const val catId: String = "EXTRA_CAT_ID"
        |
        |  public const val catName: String = "EXTRA_CAT_NAME"
        |
        |  public const val favoriteFish: String = "EXTRA_FAVORITE_FISH"
        |
        |  public fun createIntent(
        |    context: Context,
        |    catId: String,
        |    catName: String,
        |    favoriteFish: String?,
        |  ): Intent {
        |    val intent = Intent(context, CatDetailsActivity::class.java)
        |    intent.putExtra(CAT_ID, catId)
        |    intent.putExtra(CAT_NAME, catName)
        |    intent.putExtra(FAVORITE_FISH, favoriteFish)
        |    return intent
        |  }
        |}
        |
      """.trimMargin(),
      actual = compilationResult.sourceFor("CatDetailsActivityIntentFactory.kt")
    )
  }

  private fun compile(vararg source: SourceFile) = KotlinCompilation().apply {
    sources = source.toList()
    symbolProcessorProviders = listOf(ActivityParamsProcessorProvider())
    workingDir = temporaryFolder.root
    inheritClassPath = true
    verbose = false
  }.compile()

  private fun assertSourceEquals(@Language("kotlin") expected: String, actual: String) {
    assertEquals(
      expected.trimIndent(),
      actual.trimIndent().replace("\t", "    ")
    )
  }

  private fun KotlinCompilation.Result.sourceFor(fileName: String): String {
    val file = kspGeneratedSources().find { it.name == fileName }
    checkNotNull(file) { "Could not find file $fileName in ${kspGeneratedSources()}" }
    return file.readText()
  }

  private fun KotlinCompilation.Result.kspGeneratedSources(): List<File> {
    val kspWorkingDir = workingDir.resolve("ksp")
    val kspGeneratedDir = kspWorkingDir.resolve("sources")
    val kotlinGeneratedDir = kspGeneratedDir.resolve("kotlin")
    return kotlinGeneratedDir.walk().toList()
  }

  private val KotlinCompilation.Result.workingDir: File
    get() = checkNotNull(outputDirectory.parentFile)
}
