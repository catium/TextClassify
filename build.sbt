name := "TextClassify"

version := "1.0"

scalaVersion := "2.11.8"

javacOptions ++= Seq("-encoding", "UTF-8")

libraryDependencies += "commons-lang" % "commons-lang" % "2.6"
libraryDependencies += "commons-codec" % "commons-codec" % "1.3"
libraryDependencies += "tw.edu.ntu.csie" % "libsvm" % "3.1"
libraryDependencies += "de.bwaldvogel" % "liblinear" % "1.8"

libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.4"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.6.0"