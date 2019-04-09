package com.github.edeandrea.xjcplugin

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.io.TempDir
import java.io.File

abstract class AbstractUnitTests {
	@TempDir
	private lateinit var tempFolder : File

	open fun getProject(rootProjectName: String, vararg childProjects: String?) : Project {
		val projBuilder = ProjectBuilder.builder().withProjectDir(tempFolder)
		val rootProject = projBuilder.withName(rootProjectName).build()

		if (childProjects != null) {
			childProjects.forEach { childProject ->
				projBuilder
					.withName(childProject)
					.withParent(rootProject)
					.build()
					.projectDir
					.mkdirs()
			}
		}

		return rootProject
	}


}
