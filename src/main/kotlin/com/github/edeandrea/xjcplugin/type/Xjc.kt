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

package com.github.edeandrea.xjcplugin.type

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.file.FileCollection
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * A task type for performing generation of a single schema
 * @author Eric Deandrea
 */
open class Xjc : DefaultTask() {
	companion object {
		val LOGGER = Logging.getLogger(Xjc::class.java)
	}

	init {
		description = "Generates Java source files using the XJC compiler for the project '${project.name}'"
		group = "Code Generation"
	}

	/**
	 * The schema file
	 */
	@InputFiles
	@SkipWhenEmpty
	lateinit var schemaFiles: FileCollection

	/**
	 * The binding file
	 */
	@InputFile
	@Optional
	var bindingFile: File? = null

	/**
	 * The output directory for generation
	 */
	@OutputDirectory
	lateinit var schemaGenDir: File

	/**
	 * The java package name
	 */
	@Input
	@Optional
	var javaPackageName: String = ""

	/**
	 * The [SourceSet]
	 */
	@Internal
	lateinit var sourceSet: SourceSet

	private var xjcTaskAlreadyCreated = false

	private fun validateProperties() {
		if (this.schemaFiles.isEmpty) {
			throw GradleException("Property 'schemaFiles' not set on task $name")
		}
	}

	/**
	 * Generates the sources for the schema
	 */
	@TaskAction
	fun generateSources() {
		validateProperties()

		if (JavaVersion.current().isJava8Compatible) {
			System.setProperty("javax.xml.accessExternalSchema", "all")
			System.setProperty("javax.xml.accessExternalDTD", "all")
			System.setProperty("javax.xml.accessExternalStylesheet", "all")
		}

		if (!this.xjcTaskAlreadyCreated) {
			ant.invokeMethod(
				"taskdef",
				mapOf(
					"name" to "xjc",
					"classname" to "com.sun.tools.xjc.XJCTask",
					"classpath" to project.configurations.getByName("xjc").asPath
				)
			)
		}

		this.schemaFiles.forEach { schemaFile ->
			LOGGER.lifecycle("Running XJC compiler for schema $schemaFile")

			val language = if (schemaFile.name.endsWith("wsdl")) "WSDL" else "XMLSCHEMA"
			val optionsMap = mutableMapOf(
				"destdir" to this.schemaGenDir.absolutePath,
				"extension" to true,
				"schema" to schemaFile,
				"language" to language
			)

			if (this.javaPackageName.isNotBlank()) {
				optionsMap["package"] = this.javaPackageName
			}

			if (this.bindingFile != null) {
				optionsMap["binding"] = this.bindingFile
			}

			ant.invokeMethod("xjc", optionsMap)
		}
	}
}
