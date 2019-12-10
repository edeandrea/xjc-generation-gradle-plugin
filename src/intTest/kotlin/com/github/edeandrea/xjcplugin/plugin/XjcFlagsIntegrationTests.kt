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

package com.github.edeandrea.xjcplugin.plugin

import com.github.edeandrea.xjcplugin.AbstractIntegrationTests
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test

internal class XjcFlagsIntegrationTests : AbstractIntegrationTests() {
	@Test
	fun `xjcGeneration task works as expected and code compiles`() {
		val buildResult = build("clean", "build")

		listOf(":invalidUtfChars", ":compileJava", ":classes")
			.forEach { taskName ->
				assertThat(buildResult.task(taskName)?.outcome)
					.isNotNull()
					.isEqualTo(TaskOutcome.SUCCESS)
			}

		assertThat(buildResult.task(":xjcGeneration")?.outcome)
			.isNull()

		verifySchema(
			Schema(
				taskName = "invalidUtfChars",
				packageFolder = "ch/forum_datenaustausch/invoice400"
			),
			this.testDir
		)
	}
}
