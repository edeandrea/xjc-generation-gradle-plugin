package com.github.edeandrea.xjcplugin.domain

import java.io.File

class Schema(val name: String) {
	var bindingFile: String? = null
	var description: String? = null
	var javaPackageName: String = ""
	lateinit var schemaFile: String
	var sourceSet: String? = null
	var taskName: String? = null

	fun setSchemaFile(schemaFile: File) {
		this.schemaFile = schemaFile.absolutePath
	}

	fun setBindingFile(bindingFile: File) {
		this.bindingFile = bindingFile.absolutePath
	}
}
