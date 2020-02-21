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
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.withGroovyBuilder
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
	 * Indicates if the schema files should be processed by xjc in one pass or one by one.
	 * @since 1.3
	 */
	@Input
	var onePassMode: Boolean = false

	/**
	 * The schema file
	 */
	@InputFiles
	@SkipWhenEmpty
	lateinit var schemaFiles: FileCollection

	/**
	 * The schema root dir
	 * @since 1.3
	 */
	@InputDirectory
	lateinit var schemaRootDir: File

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

	/**
	 * Any additional xjc options to be passed to xjc
	 * @since 1.2
	 */
	@Input
	@Optional
	lateinit var additionalXjcOptions: Map<String, String>

	/**
	 * Any additional xjc command line args to be passed to xjc
	 * @since 1.2
	 */
	@Input
	@Optional
	lateinit var additionalXjcCommandLineArgs: Map<String, String>

	private var xjcTaskAlreadyCreated = false

	private fun validateProperties() {
		if (this.schemaFiles.isEmpty) {
			throw GradleException("Property 'schemaFiles' not set on task $name")
		}
	}

	private fun parseCommandLineArgs() = this.additionalXjcCommandLineArgs.map { (k, v) -> "$k $v".trim() }

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
			ant.withGroovyBuilder {
				"taskdef"(
					"name" to "xjc",
					"classname" to "com.sun.tools.xjc.XJCTask",
					"classpath" to project.configurations.getByName("xjc").asPath
				)
			}
		}

		if (this.onePassMode) {
			val language = guessLanguageAndDetectMixedSchemaTypes(this.schemaFiles)
			processXjc(language, null, this.schemaFiles)
		}
		else {
			this.schemaFiles
			.sorted()
			.forEach { schemaFile ->
				val language = if (schemaFile.name.endsWith("wsdl")) "WSDL" else "XMLSCHEMA"
				processXjc(language, schemaFile, null)
			}
		}
	}

	private fun processXjc(language: String, schemaFile: File?, schemaFiles: FileCollection?) {
		val optionsMap = mutableMapOf(
			"destdir" to this.schemaGenDir.absolutePath,
			"extension" to true,
			"language" to language
		)

		if (this.javaPackageName.isNotBlank()) {
			optionsMap["package"] = this.javaPackageName
		}

		if (this.bindingFile != null) {
			optionsMap["binding"] = this.bindingFile
		}

		if (this.additionalXjcOptions.isNotEmpty()) {
			this.additionalXjcOptions.forEach { (k, v) -> optionsMap.putIfAbsent(k, v) }
		}

		val commandLineArgs = parseCommandLineArgs()

		LOGGER.lifecycle("Running XJC compiler for schema(s) ${schemaFile ?: schemaFiles?.asPath}")
		LOGGER.lifecycle("\tOptions:")
		optionsMap.forEach { (k, v) ->
			LOGGER.lifecycle("\t\t$k = $v")
		}

		if (commandLineArgs.isNotEmpty()) {
			LOGGER.lifecycle("\tCommand Line Args:")
			commandLineArgs.forEach { arg ->
				LOGGER.lifecycle("\t\t$arg")
			}
		}

		if (schemaFile != null) {
			optionsMap["schema"] = schemaFile

			ant.withGroovyBuilder {
				"xjc"(optionsMap) {
					commandLineArgs.forEach { arg ->
						"arg"("value" to arg)
					}
				}
			}
		}
		else {
			ant.withGroovyBuilder {
				"xjc"(optionsMap) {
					commandLineArgs.forEach { arg ->
						"arg"("value" to arg)
					}
					schemaFiles!!.forEach {
						"schema"("file" to it)
					}
				}
			}
		}
	}
}

fun guessLanguageAndDetectMixedSchemaTypes(schemaFiles: FileCollection): String {
	return schemaFiles.fold("", ::detectMixedSchemaType)
}

private fun detectMixedSchemaType(language: String, file: File): String {
	if (!"XMLSCHEMA".equals(language) && file.name.endsWith("wsdl")) return "WSDL"
	else if (!"WSDL".equals(language) && file.name.endsWith("xsd")) return "XMLSCHEMA"
	throw GradleException("With one-pass mode it is not possible to generate classes from XMLSCHEMA and WSDL schemas. Use either XMLSCHEMA or WSDL files.")
}
