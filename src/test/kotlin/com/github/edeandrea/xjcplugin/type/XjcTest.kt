package com.github.edeandrea.xjcplugin.type

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.gradle.api.GradleException
import org.gradle.api.internal.file.collections.ImmutableFileCollection
import org.junit.jupiter.api.Test
import java.io.File

internal class XjcTest {

	@Test
	fun `XMLSCHEMA should be guessed for collection of xsd files`() {
		val schemaFiles = ImmutableFileCollection.of(File("/my/dir/schema-1.xsd"), File("/my/dir/schema-2.xsd"))
		assertThat(guessLanguageAndDetectMixedSchemaTypes(schemaFiles)).isEqualTo("XMLSCHEMA")
	}

	@Test
	fun `WSDL should be guessed for collection of wsdl files`() {
		val schemaFiles = ImmutableFileCollection.of(File("/my/dir/catalog.wsdl"), File("/my/dir/cart.wsdl"))
		assertThat(guessLanguageAndDetectMixedSchemaTypes(schemaFiles)).isEqualTo("WSDL")
	}

	@Test
	fun `Error should be thrown for collection with mixed content`() {
		val schemaFiles = ImmutableFileCollection.of(File("/my/dir/product.xsd"), File("/my/dir/catalog.wsdl"))
		assertThatThrownBy { guessLanguageAndDetectMixedSchemaTypes(schemaFiles) }.isInstanceOf(GradleException::class.java)
	}
}
