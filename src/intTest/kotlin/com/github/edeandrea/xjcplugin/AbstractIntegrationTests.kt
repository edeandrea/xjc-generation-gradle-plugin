/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.edeandrea.xjcplugin

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal abstract class AbstractIntegrationTests {
	protected lateinit var testDir: File

	@BeforeAll
	fun init(@TempDir tempFolder: File) {
		val resourceUrl = javaClass.getResource(javaClass.simpleName)
		val sourceDir = File(resourceUrl.toURI())
		this.testDir = tempFolder.absoluteFile

		println("Copying from $sourceDir to $testDir")
		sourceDir.copyRecursively(this.testDir, true)
	}

	protected fun build(vararg commands: String) : BuildResult {
		val buildResult = createAndConfigureGradleRunner(*commands).build()
		println(buildResult.output)

		return buildResult
	}

	protected fun buildAndFail(vararg commands: String) : BuildResult {
		val buildResult = createAndConfigureGradleRunner(*commands).buildAndFail()
		println(buildResult.output)

		return buildResult
	}

	protected fun createAndConfigureGradleRunner(vararg commands: String) : GradleRunner {
		return GradleRunner.create()
			.withProjectDir(this.testDir)
			.withPluginClasspath()
			.withGradleVersion(System.getProperty("gradleVersion"))
			.withArguments(listOf("-s").plus(commands.asList()))
	}
}
