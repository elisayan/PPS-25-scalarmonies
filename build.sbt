ThisBuild / scalaVersion := "3.3.5"

ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

ThisBuild / scalacOptions ++= Seq(
  "-Wunused:all"
)

lazy val root = (project in file("."))
  .settings(
    name := "PPS-25-Scalarmonies",
    scalafmtOnCompile := true,
    coverageEnabled := true,

    libraryDependencies += "junit" % "junit" % "4.13.2" % Test,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test",
    libraryDependencies += "org.scalafx" %% "scalafx" % "21.0.0-R32",
    libraryDependencies += "org.openjfx" % "javafx-controls" % "21.0.2",
    libraryDependencies += "org.openjfx" % "javafx-fxml" % "21.0.2",

    assembly / assemblyMergeStrategy := {
      case "module-info.class" => MergeStrategy.discard
      case PathList("META-INF", "substrate", _*) => MergeStrategy.discard
      case PathList("META-INF", xs @ _*) if xs.nonEmpty && (
        xs.last.endsWith(".SF") || xs.last.endsWith(".DSA") || xs.last.endsWith(".RSA")
        ) => MergeStrategy.discard
      case "META-INF/MANIFEST.MF" => MergeStrategy.discard
      case x =>
        val oldStrategy = (assembly / assemblyMergeStrategy).value
        oldStrategy(x)
    },

    Compile / mainClass := Some("it.unibo.Main"),
    assembly / mainClass := Some("it.unibo.Main"),
  )