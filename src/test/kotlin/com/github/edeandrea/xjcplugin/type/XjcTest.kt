/*
 * Copyright 2014-2020 the original author or authors.
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

package com.github.edeandrea.xjcplugin.type

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.gradle.api.GradleException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test
import java.io.File

internal class XjcTest {
	private val project = ProjectBuilder.builder()
																			.withName("myproject")
																			.build()

	@Test
	fun `XMLSCHEMA should be guessed for collection of xsd files`() {
		val schemaFiles = this.project.layout.files(File("/my/dir/schema-1.xsd"), File("/my/dir/schema-2.xsd"))
		assertThat(guessLanguageAndDetectMixedSchemaTypes(schemaFiles)).isEqualTo("XMLSCHEMA")
	}

	@Test
	fun `WSDL should be guessed for collection of wsdl files`() {
		val schemaFiles = this.project.layout.files(File("/my/dir/catalog.wsdl"), File("/my/dir/cart.wsdl"))
		assertThat(guessLanguageAndDetectMixedSchemaTypes(schemaFiles)).isEqualTo("WSDL")
	}

	@Test
	fun `Error should be thrown for collection with mixed content`() {
		val schemaFiles = this.project.layout.files(File("/my/dir/product.xsd"), File("/my/dir/catalog.wsdl"))
		assertThatThrownBy { guessLanguageAndDetectMixedSchemaTypes(schemaFiles) }.isInstanceOf(GradleException::class.java)
	}
}
