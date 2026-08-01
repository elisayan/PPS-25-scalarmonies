ThisBuild / scalaVersion := "3.3.5"

ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

ThisBuild / scalacOptions ++= Seq(
  "-Wunused:all"
)

lazy val javafxPlatforms = Seq("win", "mac", "mac-aarch64", "linux")

lazy val root = (project in file("."))
  .settings(
    name := "PPS-25-Scalarmonies",
    scalafmtOnCompile := true,

    libraryDependencies += "junit" % "junit" % "4.13.2" % Test,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test,
    libraryDependencies += "org.scalafx" %% "scalafx" % "21.0.0-R32",

    libraryDependencies ++= javafxPlatforms.flatMap { platform =>
      Seq(
        "org.openjfx" % "javafx-base"     % "21.0.2" classifier platform,
        "org.openjfx" % "javafx-graphics" % "21.0.2" classifier platform,
        "org.openjfx" % "javafx-controls" % "21.0.2" classifier platform,
        "org.openjfx" % "javafx-fxml"     % "21.0.2" classifier platform
      )
    },

    assembly / assemblyMergeStrategy := {
      case "module-info.class" =>
        MergeStrategy.discard

      case PathList("META-INF", "substrate", _*) =>
        MergeStrategy.discard

      case PathList("META-INF", xs @ _*) if xs.nonEmpty && (
        xs.last.endsWith(".SF") ||
          xs.last.endsWith(".DSA") ||
          xs.last.endsWith(".RSA")
        ) =>
        MergeStrategy.discard

      case "META-INF/MANIFEST.MF" =>
        MergeStrategy.discard

      case "javafx-swt.jar" =>
        MergeStrategy.discard

      case PathList(ps @ _*) if ps.last.endsWith(".dll") || ps.last.endsWith(".so") || ps.last.endsWith(".dylib") =>
        MergeStrategy.first

      case PathList("com", "sun", "javafx", _*)   => MergeStrategy.first
      case PathList("com", "sun", "glass", _*)    => MergeStrategy.first
      case PathList("com", "sun", "prism", _*)    => MergeStrategy.first
      case PathList("com", "sun", "scenario", _*) => MergeStrategy.first
      case PathList("com", "sun", "media", _*)    => MergeStrategy.first

      case x =>
        val oldStrategy = (assembly / assemblyMergeStrategy).value
        oldStrategy(x)
    },

    Compile / mainClass := Some("it.unibo.Main"),
    assembly / mainClass := Some("it.unibo.Main"),
  )