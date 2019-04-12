plugins {
	id("com.gradle.plugin-publish") version "0.10.1"
	id("com.github.kt3k.coveralls") version "2.8.2"
	`java-gradle-plugin`
	jacoco
	kotlin("jvm") version "1.3.21"
}

val junitVersion by extra { "5.4.2" }

group = "com.github.edeandrea"
version = "1.0"

gradlePlugin {
	plugins {
		create("xjcPlugin") {
			id = "com.github.edeandrea.xjc-generation"
			implementationClass = "com.github.edeandrea.xjcplugin.plugin.XjcPlugin"
		}
	}
}

pluginBundle {
	website = "http://github.com/edeandrea/xjc-generation-gradle-plugin"
	vcsUrl = "http://github.com/edeandrea/xjc-generation-gradle-plugin"
	description = "A Gradle Plugin for generating JAXB Java sources using the XJC compiler"

	(plugins) {
		"xjcPlugin" {
			displayName = "XJC Generation Plugin"
			tags = listOf("xjc", "jaxb")
		}
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))
	implementation(kotlin("reflect"))
	testImplementation("org.assertj:assertj-core:3.12.2")
	testImplementation(kotlin("test-junit5"))
	testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()

	reports {
		junitXml.setOutputPerTestCase(true)
	}
}

tasks {
	test {
		finalizedBy("jacocoTestReport")

		extensions.configure(JacocoTaskExtension::class) {
			setDestinationFile(file("$buildDir/coverage-results/jacoco.exec"))
		}
	}

	jacocoTestReport {
		reports {
			xml.isEnabled = true
			html.isEnabled = true
			csv.isEnabled = false
		}
	}
}

coveralls {
	jacocoReportPath = project.tasks.getByName<JacocoReport>("jacocoTestReport").reports.xml.destination
}
