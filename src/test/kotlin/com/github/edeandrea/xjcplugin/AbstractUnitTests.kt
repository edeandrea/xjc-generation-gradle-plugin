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

import org.gradle.BuildResult
import org.gradle.api.Project
import org.gradle.api.internal.GradleInternal
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.invocation.Gradle
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertFalse

/**
 * Base test class for unit tests
 * @author Eric Deandrea
 */
internal abstract class AbstractUnitTests {
	@TempDir
	internal lateinit var tempFolder : File

	open fun getProject(rootProjectName: String, vararg childProjects: String?) : Project {
		val projBuilder = ProjectBuilder.builder().withProjectDir(tempFolder)
		val rootProject = projBuilder.withName(rootProjectName).build()

		if (childProjects.isNotEmpty()) {
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

	fun runProjectBeforeEvaluate(project: Project) {
		if (project is ProjectInternal) {
			project.projectEvaluationBroadcaster.beforeEvaluate(project)
		}
	}

	fun runProjectAfterEvaluate(project: Project) {
		if (project is ProjectInternal) {
			runProjectBeforeEvaluate(project)
			project.state.toBeforeEvaluate()
			project.projectEvaluationBroadcaster.afterEvaluate(project, project.state)
		}
	}

	fun runGradleProjectsEvaluated(gradle: Gradle) {
		if (gradle is GradleInternal) {
			gradle.buildListenerBroadcaster.projectsEvaluated(gradle)
		}
	}

	fun runGradleProjectsEvaluated(project: Project) {
		runGradleProjectsEvaluated(project.gradle)
	}

	fun runGradleBuildFinished(gradle: Gradle, buildResult: BuildResult) {
		if (gradle is GradleInternal) {
			gradle.buildListenerBroadcaster.buildFinished(buildResult)
		}
	}

	fun runGradleBuildFinished(project: Project, buildResult: BuildResult) {
		runGradleBuildFinished(project.gradle, buildResult)
	}

	protected fun findException(root: Throwable?, clazz: Class<Throwable>) : Throwable? {
		if (root == null) {
			assertFalse(false, "Couldn't find exception of type ${clazz.name}")
		}

		return if (clazz.isInstance(root)) root else findException(root?.cause, clazz)
	}

	protected fun getExceptionChain(root: Throwable?) : List<Throwable> {
		val exceptionChain: List<Throwable>

		if (root != null) {
			exceptionChain = listOf(root).union(getExceptionChain(root.cause)).toList()
		}
		else {
			exceptionChain = emptyList()
		}

		return exceptionChain
	}

	protected fun getRootCause(t: Throwable) : Throwable {
		return getExceptionChain(t).last()
	}
}
