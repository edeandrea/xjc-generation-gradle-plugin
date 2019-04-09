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
	 * The collection of [schemas][Schema]
	 */
	var schemas: NamedDomainObjectContainer<Schema> = project.container(Schema::class.java)

	/**
	 * Configuration of each [Schema]
	 */
	fun schemas(configureClosure: Closure<Schema>) = this.schemas.configure(configureClosure)
}
