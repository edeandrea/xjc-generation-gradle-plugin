package com.github.edeandrea.xjcplugin.domain

import java.io.File

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
	 * The schema file to use
	 */
	lateinit var schemaFile: String

	/**
	 * The source set
	 */
	var sourceSet: String? = null

	/**
	 * The name of the task to generate
	 */
	var taskName: String? = null

	/**
	 * Override for setting the *schemaFile* as a [File]
	 * @param schemaFile The [File] reference to the *schemaFile*
	 */
	fun setSchemaFile(schemaFile: File) {
		this.schemaFile = schemaFile.absolutePath
	}

	/**
	 * Override for setting the *bindingFile* as a [File]
	 * @param bindingFile The [File] reference to the *bindingFile*
	 */
	fun setBindingFile(bindingFile: File) {
		this.bindingFile = bindingFile.absolutePath
	}
}
