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

import com.github.edeandrea.xjcplugin.AbstractIntegrationTests
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test

internal class XjcFailingMixedSchemaTypesIntegrationTests : AbstractIntegrationTests() {
	@Test
	fun `schema generation with onepass == true for mixed wsdl and xsd fails`() {
		// The buildAndFail method in AbstractIntegrationTests will do an assertion that the build as a whole failed
		val buildResult = buildAndFail("clean", "xjcGeneration")

		// Assert that the task associated with the schema generation did indeed fail
		assertThat(buildResult.task(":failingMixedSchemaTypes")?.outcome)
			.isNotNull()
			.isEqualTo(TaskOutcome.FAILED)

		// Assert that whatever error message was used in the Xjc within the GradleException thrown was produced in the build output
		assertThat(buildResult.output)
			.isNotBlank()
			.contains("With one-pass mode it is not possible to generate classes from XMLSCHEMA and WSDL schemas. Use either XMLSCHEMA or WSDL files.")
	}
}
