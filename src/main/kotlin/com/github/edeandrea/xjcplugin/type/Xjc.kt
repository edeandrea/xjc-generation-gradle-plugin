package com.github.edeandrea.xjcplugin.type

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * A task type for performing generation of a single schema
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
	@InputFile
	lateinit var schemaFile: File

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
		if (this.schemaGenDir == null) {
			throw GradleException("Property 'schemaGenDir' not set on task $name")
		}

		if (this.schemaFile == null) {
			throw GradleException("Property 'schemaFile' not set on task $name")
		}
	}

	/**
	 * Generates the sources for the schema
	 */
	@TaskAction
	fun generateSources() {
		validateProperties()

		LOGGER.lifecycle("Running XJC compiler for schema $schemaFile")

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

		val language = if (this.schemaFile.name.endsWith("wsdl")) "WSDL" else "XMLSCHEMA"
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
