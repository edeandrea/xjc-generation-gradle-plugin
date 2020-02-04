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

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.TaskOutcome
import java.io.File

data class Schema(
	val taskName: String,
	val sourceSetName: String = "main",
	val packageFolder: String,
	val schemaRootDir: String = "src/$sourceSetName/schemas/xjc",
	val expectedOutcome: TaskOutcome = TaskOutcome.SUCCESS,
	val expectedGeneratedOutputRootDir: String = "build/generated-sources/$sourceSetName/xjc"
)

fun mapFilesByName(files: Collection<File>) = files.associateBy { it.name }

fun getSourceFileContents(file: File): String {
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

fun areFilesAllEqual(actualFiles: List<File>, expectedFiles: List<File>): Boolean {
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
		println("${expectedFileNotEqual?.absolutePath}:")
		println(expectedFileNotEqual?.readText())
		println("------------------------------------------------------")
		println("${actualFileNotEqual?.absolutePath}:")
		println(actualFileNotEqual?.readText())
	}

	return allFilesEqual
}

fun verifySchema(schema: Schema, testDir: File) {
	println("Verifying schema $schema")
	val expectedFilesDir = File("$testDir/expected/${schema.schemaRootDir}/${schema.packageFolder}")
	val actualSourceFilesDir = File("$testDir/${schema.expectedGeneratedOutputRootDir}/${schema.packageFolder}")
	val expectedFiles = expectedFilesDir.walkTopDown().filter { it.isFile }.toList()
	val actualFiles = actualSourceFilesDir.walkTopDown().filter { it.isFile }.toList()

	assertThat(actualFiles).hasSameSizeAs(expectedFiles)
	assertThat(areFilesAllEqual(actualFiles, expectedFiles)).isTrue()
}
