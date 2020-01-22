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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class XjcPluginIntegrationTests : AbstractIntegrationTests() {
	private val schemasMap = mapOf(
		"generateMaven2Schema" to Schema(
			taskName = "generateMaven2Schema",
			packageFolder = "com/github/edeandrea/xjcplugin/generated/maven2"
		),
		"generateMavenSchema" to Schema(
			taskName = "generateMavenSchema",
			sourceSetName = "test",
			packageFolder = "com/github/edeandrea/xjcplugin/generated/maven"
		),
		"schemaGen_com-github-edeandrea-xjcplugin-generated-artifactory" to Schema(
			taskName = "schemaGen_com-github-edeandrea-xjcplugin-generated-artifactory",
			packageFolder = "com/github/edeandrea/xjcplugin/generated/artifactory",
			schemaRootDir = "misc/resources/schemas/artifactory"
		),
		"schemaGen_com-github-edeandrea-xjcplugin-generated-schema1" to Schema(
			taskName = "schemaGen_com-github-edeandrea-xjcplugin-generated-schema1",
			packageFolder = "com/github/edeandrea/xjcplugin/generated/schema1",
			expectedOutcome = TaskOutcome.NO_SOURCE
		),
		"schemaDirWith2Schemas" to Schema(
			taskName = "schemaDirWith2Schemas",
			packageFolder = "com/github/edeandrea/xjcplugin/generated/schemadirwith2schemas",
			sourceSetName = "test",
			schemaRootDir = "misc/resources/schemas/schemaDirWith2Schemas"
		),
		"schemaDirWith2RelatedSchemas" to Schema(
			taskName = "schemaDirWith2RelatedSchemas",
			packageFolder = "com/github/edeandrea/xjcplugin/generated/schemadirwith2relatedschemas",
			sourceSetName = "test",
			schemaRootDir = "misc/resources/schemas/schemaDirWith2RelatedSchemas"
		),
		"schemaDirWith2WSDLs" to Schema(
			taskName = "schemaDirWith2WSDLs",
			packageFolder = "com/github/edeandrea/xjcplugin/generated/schemadirwith2wsdls",
			sourceSetName = "test",
			schemaRootDir = "misc/resources/schemas/schemaDirWith2WSDLs"
		),
		"schemaDirWithNestedFolders" to Schema(
			taskName = "schemaDirWithNestedFolders",
			packageFolder = "com/github/edeandrea/xjcplugin/generated/schemadirwithnestedfolders"
		),
		"schemaWithOverriddenOutputRootDir" to Schema(
			taskName = "schemaWithOverriddenOutputRootDir",
			sourceSetName = "intTest",
			packageFolder = "com/github/edeandrea/xjcplugin/generated/overriddenoutputroot",
			expectedGeneratedOutputRootDir = "build/generated-output"
		),
		"schemaWithOverriddenOutputRootDirFile" to Schema(
			taskName = "schemaWithOverriddenOutputRootDirFile",
			sourceSetName = "intTest",
			packageFolder = "com/github/edeandrea/xjcplugin/generated/overriddenoutputrootfile",
			expectedGeneratedOutputRootDir = "build/generated-output"
		)
	)

	private fun schemas() = schemasMap.values

	@Test
	fun `xjcGeneration task works as expected`() {
		val buildResult = build("clean", "xjcGeneration")

		this.schemasMap.values
			.map { it.taskName }
			.plus("xjcGeneration")
			.forEach { taskName ->
				val expectedOutcome = if (taskName == "xjcGeneration") TaskOutcome.SUCCESS else this.schemasMap[taskName]!!.expectedOutcome

				assertThat(buildResult.task(":$taskName")?.outcome)
					.isNotNull()
					.isEqualTo(expectedOutcome)
		}

		this.schemasMap.values.forEach { schema -> verifySchema(schema, this.testDir)}
	}

	@ParameterizedTest(name = "{index} ==> XJC generation for schema {0} should be correct")
	@MethodSource("schemas")
	fun `XJC generation for task should produce the proper results`(schema: Schema) {
		val buildResult = build("clean", schema.taskName)

		assertThat(buildResult.task(":${schema.taskName}")?.outcome)
			.isNotNull()
			.isEqualTo(schema.expectedOutcome)

		assertThat(buildResult.task(":xjcGeneration")?.outcome)
			.isNull()

		verifySchema(schema, this.testDir)
	}
}
