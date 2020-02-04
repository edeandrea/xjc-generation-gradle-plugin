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

package com.github.edeandrea.xjcplugin.domain

import groovy.lang.Closure
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import java.io.File

/**
 * An extension for holding the *xjcGeneration* DSL
 *
 * @property project The [Project]
 * @author Eric Deandrea
 */
open class XjcExtension(project: Project) {
	/**
	 * The default source set to use for all [schemas][Schema] if any individual [Schema] does not specify one. Defaults to **main**.
	 */
	var defaultSourceSet: String = "main"

	/**
	 * The default binding file to use for all [schemas][Schema] if any individual [Schema] does not specify one.
	 */
	var defaultBindingFile: File? = null

	/**
	 * Default additional xjc options to be passed to xjc
	 * @since 1.2
	 */
	var defaultAdditionalXjcOptions: Map<String, String> = mutableMapOf()

	/**
	 * Default additional xjc command line to be passed to xjc
	 * @since 1.2
	 */
	var defaultAdditionalXjcCommandLineArgs: Map<String, String> = mutableMapOf()

	/**
	 * The collection of [schemas][Schema]
	 */
	var schemas: NamedDomainObjectContainer<Schema> = project.container(Schema::class.java)

	/**
	 * Configuration of each [Schema]
	 */
	fun schemas(configureClosure: Closure<Schema>) = this.schemas.configure(configureClosure)
}

/**
 * Placeholder for a schema within the *xjcGeneration* DSL
 * @property name The name of the schema
 */
class Schema(val name: String) {
	/**
	 * The binding file to use
	 */
	var bindingFile: String? = null

	/**
	 * The description
	 */
	var description: String? = null

	/**
	 * The java package name
	 */
	var javaPackageName: String = ""

	/**
	 * The schema file to use. Either this or [schemaDir] needs to be set, but not both
	 */
	var schemaFile: String? = null

	/**
	 * The source set
	 */
	var sourceSet: String? = null

	/**
	 * The name of the task to generate
	 */
	var taskName: String? = null

	/**
	 * The schema root directory
	 * @since 1.1
	 */
	var schemaRootDir: String? = null

	/**
	 * The schema directory to use. Either this or [schemaFile] needs to be set, but not both
	 * @since 1.1
	 */
	var schemaDir: String? = null

	/**
	 * Any additional xjc options to be passed to xjc
	 * @since 1.2
	 */
	var additionalXjcOptions: Map<String, String> = mutableMapOf()

	/**
	 * Any additional xjc command line args to be passed to xjc
	 * @since 1.2
	 */
	var additionalXjcCommandLineArgs: Map<String, String> = mutableMapOf()

	/**
	 * The root directory for generated source output
	 * @since 1.3
	 */
	var generatedOutputRootDir: String? = null

	/**
	 * Override for setting the [schemaFile] as a [File]
	 * @param schemaFile The [File] reference to the [schemaFile]
	 */
	fun setSchemaFile(schemaFile: File) {
		this.schemaFile = schemaFile.absolutePath
	}

	/**
	 * Override for setting the [schemaRootDir] as a [File]
	 * @param schemaRootDir The [File] reference to the [schemaRootDir]
	 * @since 1.1
	 */
	fun setSchemaRootDir(schemaRootDir: File) {
		this.schemaRootDir = schemaRootDir.absolutePath
	}

	/**
	 * Override for setting the [schemaDir] as a [File]
	 * @param schemaDir The [File] reference to the [schemaDir]
	 * @since 1.1
	 */
	fun setSchemaDir(schemaDir: File) {
		this.schemaDir = schemaDir.absolutePath
	}

	/**
	 * Override for setting the *bindingFile* as a [File]
	 * @param bindingFile The [File] reference to the *bindingFile*
	 */
	fun setBindingFile(bindingFile: File) {
		this.bindingFile = bindingFile.absolutePath
	}

	/**
	 * Override for setting the *generatedSourcesOutputRootDir* as a [File]
	 * @param generatedOutputRootDir The [File] reference to the *generatedSourcesOutputRootDir*
	 * @since 1.3
	 */
	fun setGeneratedOutputRootDir(generatedOutputRootDir: File) {
		this.generatedOutputRootDir = generatedOutputRootDir.absolutePath
	}
}
