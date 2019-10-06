name := """file-archive"""
organization := "ru.oav"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.0"

libraryDependencies += guice

libraryDependencies += javaJpa
libraryDependencies += evolutions
//libraryDependencies += "com.h2database" % "h2" % "1.4.197"
//Driver for mysql
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.36"
libraryDependencies += "org.hibernate" % "hibernate-core" % "5.4.0.Final"

PlayKeys.externalizeResourcesExcludes += baseDirectory.value / "conf" / "META-INF" / "persistence.xml"