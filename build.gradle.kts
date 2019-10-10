plugins {
	id("com.gradle.plugin-publish") version "0.10.1"
	`java-gradle-plugin`
	idea
	kotlin("jvm") version "1.3.50"
}

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions {
		jvmTarget = "1.8"
		apiVersion = "1.3"
		languageVersion = "1.3"
	}
}

val junitVersion by extra { "5.5.2" }

group = "com.github.edeandrea"
version = "1.1"

sourceSets {
	create("intTest") {
		compileClasspath += sourceSets.main.get().output
		runtimeClasspath += sourceSets.main.get().output
	}
}

val intTestImplementation by configurations.getting {
	extendsFrom(configurations.implementation.get())
}

val intTestRuntimeOnly by configurations.getting {
	extendsFrom(configurations.runtimeOnly.get())
}

gradlePlugin {
	testSourceSets(sourceSets["intTest"])

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

idea {
	project {
		jdkName = "1.8"
	}

	module {
		isDownloadJavadoc = true
		isDownloadSources = true
		testSourceDirs.addAll(sourceSets["intTest"].allSource.srcDirs)
		testResourceDirs.addAll(sourceSets["intTest"].resources.srcDirs)
		val testScope = scopes["TEST"]

		if (testScope != null) {
			testScope["plus"]?.add(configurations["intTestCompile"])
		}
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))
	implementation(kotlin("reflect"))
	testImplementation("org.assertj:assertj-core:3.13.2")
	testImplementation(kotlin("test-junit5"))
	testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
	intTestImplementation("org.assertj:assertj-core:3.13.2")
	intTestImplementation(kotlin("test-junit5"))
	intTestImplementation(gradleTestKit())
	intTestImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
	intTestImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
	intTestRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

task<Test>("integrationTest") {
	description = "Runs integration tests"
	group = "verification"
	testClassesDirs = sourceSets["intTest"].output.classesDirs
	classpath = sourceSets["intTest"].runtimeClasspath
	shouldRunAfter("test")

	afterEvaluate {
		systemProperty("gradle.user.home", "$buildDir/tmp/TestKit")
		systemProperty("gradleVersion", gradle.gradleVersion)
		systemProperty("testKitGradlePropsFile", "$buildDir/tmp/testkit-gradle.properties")
	}
}

tasks.check {
	dependsOn("integrationTest")
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()

	testLogging {
		events("passed", "failed", "skipped")
	}

	reports {
		junitXml.isOutputPerTestCase = true
	}
}
