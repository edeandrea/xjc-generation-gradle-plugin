![](https://github.com/edeandrea/xjc-generation-gradle-plugin/workflows/Build%20app/badge.svg)

# xjc-generation-gradle-plugin
A Gradle Plugin for generating JAXB Java sources using the XJC compiler. Under the covers uses the [XJC Ant Task](https://javaee.github.io/jaxb-v2/doc/user-guide/ch04.html#tools-xjc-ant-task).

This plugin offers a flexible approach for allowing JAXB source generation using the XJC compiler. The core concept is to allow configuration of dependencies specific to the classpath of the XJC compiler while allowing the generated sources to end up in any particular `sourceSet` that the project may define.

## Installation
You can use the `plugins` closure

**Groovy**
```groovy
plugins {
  id 'com.github.edeandrea.xjc-generation' version '1.5'
}
```

**Kotlin**
```kotlin
plugins {
  id("com.github.edeandrea.xjc-generation") version "1.5"
}
```

## Configuration
The plugin creates a new configuration called `xjc` which you need to wire in the necessary dependencies for the JAXB API itself as well as the XJC compiler. **NOTE:** The dependencies you use will depend on the JAXB version you are using.

**Groovy - JAXB 2.2.x**
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

**Groovy - JAXB 2.3.x**
```groovy
ext {
  jaxbVersion = '2.3.1'
}

dependencies {
  xjc "javax.xml.bind:jaxb-api:$jaxbVersion"
  xjc "org.glassfish.jaxb:jaxb-runtime:$jaxbVersion"
  xjc "org.glassfish.jaxb:jaxb-xjc:$jaxbVersion"
}
```

**Kotlin - JAXB 2.2.x**
```kotlin
val jaxbVersion by extra { "2.2.11" }

dependencies {
  "xjc"("javax.xml.bind:jaxb-api:$jaxbVersion")
  "xjc"("com.sun.xml.bind:jaxb-impl:$jaxbVersion")
  "xjc"("com.sun.xml.bind:jaxb-xjc:$jaxbVersion")
  "xjc"("com.sun.xml.bind:jaxb-core:$jaxbVersion")
  "xjc"("javax.activation:activation:1.1.1")
}
```

**Kotlin - JAXB 2.3.x**
```kotlin
val jaxbVersion by extra { "2.3.1" }

dependencies {
  "xjc"("javax.xml.bind:jaxb-api:$jaxbVersion")
  "xjc"("org.glassfish.jaxb:jaxb-runtime:$jaxbVersion")
  "xjc"("org.glassfish.jaxb:jaxb-xjc:$jaxbVersion")
}
```

Once that's done you can configure the `xjcGeneration` DSL as shown below. All the configuration options are shown with their default values.

**Groovy**
```groovy
xjcGeneration {
  defaultBindingFile = null  // A File reference to a default binding file to be used for all schemas
  defaultSourceSet = 'main'  // The default sourceSet for all schemas to be generated from
  defaultAdditionalXjcOptions = [:]  // A Map containing additional options to pass to xjc for all schemas. If the option doesn't have a value, then use the empty string as a value. Available since version 1.2.
  defaultAdditionalXjcCommandLineArgs = [:]  // A Map containing additional command line args to pass to xjc for all schemas. If the option doesn't have a value, then use the empty string as a value. Available since version 1.2.
  
  schemas {
    // Here you can create as many schemas as you would like. Each has to have a unique top-level name which can be whatever you choose
    // In this example we've chosen the name 'myschema', but it could be any label you wanted
    myschema {
      bindingFile = null  // Can be either a String or File reference to a binding file to use for this schema. If null or empty, the default binding file will be used.
      description = null  // A description of the schema
      javaPackageName = ""  // The java package to generate the sources under
      schemaRootDir = "$projectDir/src/$sourceSet/schemas/xjc"  // A String or File reference to serve as the root directory holding the schema. Available since version 1.1.
      schemaFile = null  // A String or File reference that is relative to schemaRootDir containing the location of the file to generate sources from. Only this or schemaDir can be used, not both.
      schemaDir = null  // A String or File reference that is relative to schemaRootDir containing a folder to generate sources from. This folder is searched recursively and all files found are used.  Only this or schemaFile can be used, not both. Available since version 1.1.
      sourceSet = null  // The String name of the source set for this schema. If null or empty, the default source set will be used
      onePassMode = false // Define if the generation of classes should be performed in one pass (all schema files in the directory together or one by one). If not defined, the files are processed one by one. Available since version 1.3.
      taskName = null  // Optionally define a task name to be used for the generation of this schema. If null or empty a default one will be created
      generatedOutputRootDir = "$buildDir/generated-sources/$sourceSet/xjc" // A String or File reference defining the root output directory for generated sources. Available since version 1.3.
      additionalXjcOptions = [:]  // A Map containing additional options to pass to xjc for this schema. Any options here will override anything in defaultAdditionalXjcOptions. If the option doesn't have a value, then use the empty string as a value. Available since version 1.2.
      additionalXjcCommandLineArgs = [:]  // A Map containing additional command line args to pass to xjc for this schema. Any options here will override anything in defaultAdditionalXjcCommandLineArgs. If the option doesn't have a value, then use the empty string as a value. Available since version 1.2.
    }
  }
}
```

**Kotlin**
```kotlin
xjcGeneration {
  defaultBindingFile = null
  defaultSourceSet = "main"
  defaultAdditionalXjcOptions = mapOf()
  defaultAdditionalXjcCommandLineArgs = mapOf()

  schemas {
    create("myschema") {
      bindingFile = null
      description = null
      javaPackageName = ""
      schemaRootDir = "$projectDir/src/$sourceSet/schemas/xjc"
      schemaFile = null
      schemaDir = null
      sourceSet = null
      onePassMode = false
      taskName = null
      generatedOutputRootDir = "$buildDir/generated-sources/$sourceSet/xjc"
      additionalXjcOptions = mapOf()
      additionalXjcCommandLineArgs = mapOf()
    }
  }
}
```

You want to place your schema files in the `src/${sourceSet}/schemas/xjc` folder, unless a particular schema has defined a `schemaRootDir` (`schemaRootDir` available as of version `1.1`). After generation happens, the generated sources will end up in the `${buildDir}/generated-sources/${sourceSet}/xjc` folder, or as of version 1.3 they would be in the directory defined by a schema's `generatedOutputRootDir`. They should be linked as a source/output directory in your IDE as well.

For each individual schema, the generation of that schema will happen prior to the `compile` task for the particular source set the schema is tied to (i.e. schemas for the `main` source set will happen before the `compileJava` task, schemas for the `test` source set will happen before the `compileTestJava` task(. There is also a single task called `xjcGeneration` that is introduced which will run the generation for all schemas.

## Example Usage
**Groovy** `build.gradle` file
```groovy
plugins {
  id 'com.github.edeandrea.xjc-generation' version '1.5'
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
  defaultAdditionalXjcOptions = ['encoding': 'UTF-8']
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
  
    someThirdSchema {
      schemaFile = 'some-third-schema-1.0/somethirdschema-1.0.xsd'
      javaPackageName = 'com.anothercompany.somethirdpackage.generated'
      schemaRootDir = 'misc/resources/schemas'
    }

    someFourthSchema {
      schemaDir = 'some-schema-dir'
      javaPackageName = ' com.fourthcompany.somepackage.generated'
    }

    someFifthSchema {
      schemaFile = 'some-other-schema-dir/some-schema.xsd'
      javaPackageName = 'com.someotherpackage'
      additionalXjcOptions = ['encoding': 'EUC-JP']
      additionalXjcCommandLineArgs = ['-verbose': '']
    }
    
   overriddenOutputDir {
      schemaFile = 'another-schema-1.0/another-schema-1.0.xsd'
      javaPackageName = 'com.github.edeandrea.generated.overriddenoutputdir'
      generatedOutputRootDir = file "$buildDir/generated"  // This could also be generatedOutputRootDir = "$buildDir/generated" 
    }

    someSixthSchema {
      schemaDir = 'sixth-schemas'
      schemaRootDir = 'misc/resources/schemas'
      onePassMode = true
    }
  }
}
```

**Kotlin** `build.gradle.kts` file
```kotlin
plugins {
  id("com.github.edeandrea.xjc-generation") version "1.5"
}

val jaxbVersion by extra { "2.2.11" }

dependencies {
  "xjc"("javax.xml.bind:jaxb-api:$jaxbVersion")
  "xjc"("com.sun.xml.bind:jaxb-impl:$jaxbVersion")
  "xjc"("com.sun.xml.bind:jaxb-xjc:$jaxbVersion")
  "xjc"("com.sun.xml.bind:jaxb-core:$jaxbVersion")
  "xjc"("javax.activation:activation:1.1.1")
}

xjcGeneration {
  defaultAdditionalXjcOptions = mapOf("encoding" to "UTF-8")
  defaultBindingFile = file("src/main/schemas/xjc/xjc.xjb.xml")
    
  schemas {
    create("maven") {
      schemaFile = "maven-4.0/maven-4.0.0.xsd"
      javaPackageName = "com.github.edeandrea.generated.maven"
    }
  
    create("anotherSchema") {
      schemaFile = "some-other-schema-1.0/someschema-1.0.wsdl"
      javaPackageName = "com.somecompany.someschema.generated"
      sourceSet = "test"
    }
 
    create("someThirdSchema") {
      schemaFile = "some-third-schema-1.0/somethirdschema-1.0.xsd"
      javaPackageName = "com.anothercompany.somethirdpackage.generated"
      schemaRootDir = "misc/resources/schemas"
    }

    create("someFourthSchema") {
      schemaDir = "some-schema-dir"
      javaPackageName = "com.fourthcompany.somepackage.generated"
    }

    create("someFifthSchema") {
      schemaFile = "some-other-schema-dir/some-schema.xsd"
      javaPackageName = "com.someotherpackage"
      additionalXjcOptions = mapOf("encoding" to "EUC-JP")
      additionalXjcCommandLineArgs = mapOf("-verbose" to "")
    }
 
    create("overriddenOutputDir") {
      schemaFile = "another-schema-1.0/another-schema-1.0.xsd"
      javaPackageName = "com.github.edeandrea.generated.overriddenoutputdir"
      generatedOutputRootDir = file("$buildDir/generated")  // This could also be generatedOutputRootDir = "$buildDir/generated"
    }

    create("someSixthSchema") {
      schemaDir = "sixth-schemas"
      schemaRootDir = "misc/resources/schemas"
      onePassMode = true
    }
  }
}
```

For this example to work you would have the following filesystem layout

```
/
    misc/
        resources/
            schemas/
                some-third-schema-1.0/
                    somethirdschema-1.0.xsd
                sixth-schemas/
                    somesixthschema1.xsd
                    somesixthschema2.xsd
    src/
        main/
            schemas/
                xjc/
                    another-schema-1.0/
                        another-schema-1.0.xsd
                    maven-4.0/
                        maven-4.0.0.xsd
                    some-other-schema-dir/
                        some-schema.xsd
                    some-schema-dir/
                        nestedfolder1/
                            nestedfolder2/
                                schema.xsd
                            anotherschema.wsdl
                        fourthschema.xsd
                        someWsdl.wsdl
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
        generated/
            com/
                github/
                    edeandrea/
                        generated/
                            overriddenoutputdir/
                                All generated .class files for the another-schema-1.0.xsd in here
        generated-sources/
            main/
                xjc/
                    com/
                        anothercompany/
                            somethirdpackage/
                                generated/
                                    All generated .class files for the somethirdschema-1.0.xsd schema in here
                        fourthcompany/
                            somepackage/
                                generated/
                                    All generated .class files for fourthschema.xsd, someWsdl.wsdl, anotherschema.wsdl, and schema.xsd
                        github/
                            edeandrea/
                                generated/
                                    maven/
                                        All generated .class files for the maven-4.0.0.xsd schema in here
                        sixthpackage/
                            generated/
                                part1/
                                    All generated .class files for the somesixthschema1.xsd schema in here
                                part2/
                                    All generated .class files for the somesixthschema2.xsd schema in here
                        someotherpackage/
                            All generated .class files for some-schema.xsd schema in here
            test/
                xjc/
                    com/
                        somecompany/
                            someschema/
                                generated/
                                    All generated .class files for the someschema-1.0.wsdl WSDL in here
```
