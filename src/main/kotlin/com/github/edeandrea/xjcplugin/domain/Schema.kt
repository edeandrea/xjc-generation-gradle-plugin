package com.github.edeandrea.xjcplugin.domain

import java.io.File

class Schema(val name: String) {
	var description: String? = null
	var taskName: String? = null
	lateinit var schemaFile: String
	var javaPackageName: String? = null
	var sourceSet: String? = null
	var bindingFile: String? = null

	fun setSchemaFile(schemaFile: File) {
		this.schemaFile = schemaFile.absolutePath
	}

	fun setBindingFile(bindingFile: File) {
		this.bindingFile = bindingFile.absolutePath
	}
}
