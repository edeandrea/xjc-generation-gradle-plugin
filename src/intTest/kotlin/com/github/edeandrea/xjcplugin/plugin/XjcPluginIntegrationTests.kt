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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.util.stream.Stream

internal class XjcPluginIntegrationTests : AbstractIntegrationTests() {
	@ParameterizedTest(name = "{index} ==> XJC generation for task ''{0}'' should produce the package ''{1}'' in the ''{2}'' source set")
	@MethodSource("xjcGenerationParams")
	fun `XJC generation for task should produce the proper results`(taskName: String, sourceSetName: String, packageName: String) {
		build("clean", taskName)
		val expectedFilesDir = File("$testDir/expected/src/$sourceSetName/schemas/xjc/$packageName")
		val actualSourceFilesDir = File("$testDir/build/generated-sources/$sourceSetName/xjc/$packageName")
		val expectedFiles = expectedFilesDir.listFiles().toList()
		val actualFiles = actualSourceFilesDir.listFiles().toList()

		assertThat(expectedFiles).hasSameSizeAs(actualFiles)
		assertThat(areFilesAllEqual(actualFiles, expectedFiles)).isTrue()
	}

	fun xjcGenerationParams(): Stream<Arguments> {
		return Stream.of(
			Arguments.of("xjcGeneration", "main", "com/github/edeandrea/xjcplugin/generated/maven2"),
			Arguments.of("xjcGeneration", "test", "com/github/edeandrea/xjcplugin/generated/maven"),
			Arguments.of("generateMaven2Schema", "main", "com/github/edeandrea/xjcplugin/generated/maven2"),
			Arguments.of("generateMavenSchema", "test", "com/github/edeandrea/xjcplugin/generated/maven")
		)
	}

	private fun mapFilesByName(files: Collection<File>) = files.associateBy { it.name }

	private fun getSourceFileContents(file: File): String {
		var collectLine = false
		val lines = file.readLines().map { line ->
			if (!collectLine && line.startsWith("package ")) {
				collectLine = true
			}

			if (collectLine) line else null
		}.toMutableList()

		lines.removeAll { (it == null) || it.trim().isBlank() }
		return lines.joinToString(separator = "\n")
	}

	private fun areFilesAllEqual(actualFiles: List<File>, expectedFiles: List<File>): Boolean {
		val actualFilesMap = mapFilesByName(actualFiles)
		var allFilesEqual = true
		var expectedFileNotEqual: File? = null
		var actualFileNotEqual: File? = null

		for (file in expectedFiles) {
			val actualFile = actualFilesMap[file.name]

			if ((actualFile == null) || (getSourceFileContents(file) != getSourceFileContents(actualFile))) {
				allFilesEqual = false
				expectedFileNotEqual = file
				actualFileNotEqual = actualFile
				break
			}
		}

		if (!allFilesEqual) {
			println("Contents of expected file ${expectedFileNotEqual?.absolutePath} and actual file ${actualFileNotEqual?.absolutePath} aren't the same")
		}

		return allFilesEqual
	}
}
