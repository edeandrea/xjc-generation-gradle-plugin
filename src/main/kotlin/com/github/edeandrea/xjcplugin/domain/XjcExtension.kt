package com.github.edeandrea.xjcplugin.domain

import groovy.lang.Closure
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import java.io.File

open class XjcExtension(project: Project) {
	var defaultSourceSet: String = "main"
	var defaultBindingFile: File? = null
	var schemas: NamedDomainObjectContainer<Schema> = project.container(Schema::class.java)

	fun schemas(configureClosure: Closure<Schema>) = this.schemas.configure(configureClosure)
}
