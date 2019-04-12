[![Build Status](https://travis-ci.org/edeandrea/xjc-generation-gradle-plugin.svg?branch=master)](https://travis-ci.org/edeandrea/xjc-generation-gradle-plugin)

# xjc-generation-gradle-plugin
A Gradle Plugin for generating JAXB Java sources using the XJC compiler

This plugin offers a flexible approach for allowing JAXB source generation using the XJC compiler. The core concept is to allow configuration of dependencies specific to the classpath of the XJC compiler while allowing the generated sources to end up in any particular `sourceSet` that the project may define.

## Installation
You can use the `plugins` closure

**Groovy**
```groovy
plugins {
	id 'com.github.edeandrea.xjc-generation' version '1.0'
}
```

**Kotlin**
```kotlin
plugins {
  id("com.github.edeandrea.xjc-generation") version "1.0"
}
```

## Configuration
The plugin creates a new configuration called `xjc` which you need to wire in the necessary dependencies for the JAXB API itself as well as the XJC compiler.

```groovy
ext {
	jaxbVersion = '2.2.11'
}

dependencies {
	xjc "javax.xml.bind:jaxb-api:$jaxbVersion"
	xjc "com.sun.xml.bind:jaxb-impl:$jaxbVersion"
	xjc "com.sun.xml.bind:jaxb-xjc:$jaxbVersion"
	xjc "com.sun.xml.bind:jaxb-core:$jaxbVersion"
	xjc 'javax.activation:activation:1.1.1'
}
```

Once that's done you can configure the `xjcGeneration` DSL as shown below. All the configuration options are shown with their default values.

```groovy
xjcGeneration {
	defaultBindingFile = null  // A File reference to a default binding file to be used for all schemas
	defaultSourceSet = 'main'  // The default sourceSet for all schemas to be generated from

	schemas {
		// Here you can create as many schemas as you would like. Each has to have a unique top-level name which can be whatever you choose
		// In this example we've chosen the name 'myschema', but it could be any label you wanted
		myschema {
			bindingFile = null  // Can be either a String or File reference to a binding file to use for this schema. If null or empty, the default binding file will be used.
			description = null  // A description of the schema
			javaPackageName = ""  // The java package to generate the sources under
			schemaFile = null  // A String reference that is relative to src/main/schemas/xjc
			sourceSet = null  // The name of the source set for this schema. If null or empty, the default source set will be used
			taskName = null  // Optionally define a task name to be used for the generation of this schema. If null or empty a default one will be created
		}
	}
}
```

You want to place your schema files in the `src/${sourceSet}/schemas/xjc` folder. After generation happens, the generated sources will end up in the `${buildDir}/generated-sources/${sourceSet}/xjc` folder. They should be linked as a source/output directory in your IDE as well.

For each individual schema, the generation of that schema will happen prior to the `compile` task for the particular source set the schema is tied to (i.e. schemas for the `main` source set will happen before the `compileJava` task, schemas for the `test` source set will happen before the `compileTestJava` task(. There is also a single task called `xjcGeneration` that is introduced which will run the generation for all schemas.

## Example Usage
`build.gradle` file

```groovy
plugins {
	id 'com.github.edeandrea.xjc-generation' version '1.0'
}

ext {
	jaxbVersion = '2.2.11'
}

dependencies {
	xjc "javax.xml.bind:jaxb-api:$jaxbVersion"
	xjc "com.sun.xml.bind:jaxb-impl:$jaxbVersion"
	xjc "com.sun.xml.bind:jaxb-xjc:$jaxbVersion"
	xjc "com.sun.xml.bind:jaxb-core:$jaxbVersion"
	xjc 'javax.activation:activation:1.1.1'
}

xjcGeneration {
	defaultBindingFile = file 'src/main/schemas/xjc/xjc.xjb.xml'

	schemas {
		maven {
			schemaFile = 'maven-4.0/maven-4.0.0.xsd'
			javaPackageName = 'com.github.edeandrea.generated.maven'
		}
		
		anotherSchema {
			schemaFile = 'some-other-schema-1.0/someschema-1.0.wsdl'
			javaPackageName = 'com.somecompany.someschema.generated'
			sourceSet = 'test'
		}
	}
}
```

For this example to work you would have the following filesystem layout

```
/
	src/
		main/
			schemas/
				xjc/
					maven-4.0/
						maven-4.0.0.xsd
					xjc.xjb.xml
		test/
			schemas/
				xjc/
					some-other-schema-1.0/
						someschema-1.0.wsdl
```

After running the generation the output would be

```
/
	${buildDir}/
		generated-sources/
			main/
				xjc/
					com/
						github/
							edeandrea/
								generated/
									maven/
										All generated .class files in here
			test/
				xjc/
					com/
						somecompany/
							someschema/
								generated/
									All generated .class files in here
```
