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

package com.github.edeandrea.xjcplugin

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.io.TempDir
import java.io.File

/**
 * Base test class for unit tests
 * @author Eric Deandrea
 */
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
