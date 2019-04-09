package com.github.edeandrea.xjcplugin.type

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction
import java.io.File

open class Xjc : DefaultTask() {
	companion object {
		val LOGGER = Logging.getLogger(Xjc::class.java)
	}

	init {
		description = "Generates Java source files using the XJC compiler for the project '${project.name}'"
		group = "Code Generation"
	}

	@InputFile
	lateinit var schemaFile: File

	@InputFile
	var bindingFile: File? = null

	@OutputDirectory
	lateinit var schemaGenDir: File

	@Input
	var javaPackageName: String? = ""

	@Internal
	lateinit var sourceSet: SourceSet

	private var xjcTaskAlreadyCreated = false

	private fun validateProperties() {
		if (this.schemaGenDir == null) {
			throw GradleException("Property 'schemaGenDir' not set on task $name")
		}

		if (this.bindingFile == null) {
			throw GradleException("Property 'bindingFile' not set on task $name")
		}

		if (this.schemaFile == null) {
			throw GradleException("Property 'schemaFile' not set on task $name")
		}
	}

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

		if ((this.javaPackageName == null) || this.javaPackageName!!.isBlank()) {
			ant.invokeMethod(
				"xjc",
				mapOf(
					"destdir" to this.schemaGenDir.absolutePath,
					"binding" to this.bindingFile,
					"extension" to true,
					"schema" to this.schemaFile,
					"language" to language
				)
			)
		}
		else {
			ant.invokeMethod(
				"xjc",
				mapOf(
					"destdir" to this.schemaGenDir.absolutePath,
					"binding" to this.bindingFile,
					"package" to this.javaPackageName,
					"extension" to true,
					"schema" to schemaFile,
					"language" to language
				)
			)
		}
	}
}
