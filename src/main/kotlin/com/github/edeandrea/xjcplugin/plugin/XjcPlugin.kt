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

import com.github.edeandrea.xjcplugin.domain.Schema
import com.github.edeandrea.xjcplugin.domain.XjcExtension
import com.github.edeandrea.xjcplugin.type.Xjc
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.SourceSetContainer

/**
 * The main plugin class
 * @author Eric Deandrea
 */
class XjcPlugin : Plugin<Project> {
	companion object {
		val log = Logging.getLogger(XjcPlugin::class.java)
	}

	override fun apply(project: Project) {
		project.pluginManager.apply("java")
		project.configurations.maybeCreate("xjc")
		project.extensions.create("xjcGeneration", XjcExtension::class.java, project)

		project.task(
			mapOf(
				"group" to "Code Generation",
				"description" to "Run all XJC tasks",
				"dependsOn" to project.tasks.withType(Xjc::class.java)
			),
			"xjcGeneration"
		)

		project.afterEvaluate {
			createTasksFromSchemas(project)
		}
	}

	private fun createTasksFromSchemas(project: Project) {
		val xjcExtension = project.extensions.getByType(XjcExtension::class.java)
		val sourceSets = project.extensions.getByType(SourceSetContainer::class.java)

		xjcExtension.schemas?.forEach { schema ->
			if ((schema.schemaFile == null) || schema.schemaFile.isBlank()) {
				throw GradleException("Required property 'schemaFile' for schema '${schema.name}' not set")
			}

			val taskName = getTaskName(schema)
			val sourceSetName = schema.sourceSet ?: xjcExtension.defaultSourceSet
			val sourceSet = sourceSets.getByName(sourceSetName)
			val schemaRootDir = schema.schemaRootDir ?: "${project.projectDir}/src/$sourceSetName/schemas/xjc"
			val schemaDir = project.file(schemaRootDir)
			val schemaFile = project.file("$schemaDir/${schema.schemaFile}")
			val generatedSourcesDir = "${project.buildDir}/generated-sources/$sourceSetName/xjc"
			val bindingFile = if (schema.bindingFile != null) project.file("${project.projectDir}/${schema.bindingFile}") else xjcExtension.defaultBindingFile
			val taskDesc = schema.description ?: "Generate sources for the schema file $schemaFile"

			log.info("------------------------------------------")
			log.info("taskName = $taskName")
			log.info("sourceSetName = $sourceSetName")
			log.info("schemaRootDir = $schemaRootDir")
			log.info("schemaDir = $schemaDir")
			log.info("schemaFile = $schemaFile")
			log.info("generatedSourcesDir = $generatedSourcesDir")
			log.info("bindingFile = $bindingFile")
			log.info("taskDesc = $taskDesc")
			log.info("------------------------------------------")

			sourceSet.java.srcDir(generatedSourcesDir)

			val xjcTask = project.tasks.create(taskName, Xjc::class.java) {
				it.description = taskDesc
				it.schemaFile = schemaFile
				it.javaPackageName = schema.javaPackageName
				it.sourceSet = sourceSet
				it.bindingFile = bindingFile
				it.schemaGenDir = project.file(generatedSourcesDir)
			}

			val sourceSetNameTaskName = if (sourceSetName == "main") "compileJava" else "compile${sourceSetName.capitalize()}Java"
			project.tasks.getByName(sourceSetNameTaskName).dependsOn(xjcTask)
		}
	}

	private fun getTaskName(schema: Schema): String {
		val schemaGenTaskNameBeginning = "schemaGen_"

		if (schema.taskName == null) {
			val javaPackageName = schema.javaPackageName

			if (javaPackageName != null) {
				val replacedPackageName = javaPackageName.replace(".", "-")
				return "${schemaGenTaskNameBeginning}${replacedPackageName}"
			}
			else {
				val replacedSchemaFile = schema.schemaFile.replace(" ", "-")
				return "${schemaGenTaskNameBeginning}${replacedSchemaFile}"
			}
		}

		return schema.taskName!!
	}
}
