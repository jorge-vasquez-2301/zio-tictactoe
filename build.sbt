val scalaVer = "3.1.3"

val zioVersion = "2.0.0-RC6"

lazy val compileDependencies = Seq(
  "dev.zio" %% "zio" % zioVersion
) map (_     % Compile)

lazy val settings = Seq(
  name         := "zio-tictactoe",
  version      := "2.0.0",
  scalaVersion := scalaVer,
  scalacOptions ++= Seq(
    "-language:postfixOps",
    "-Ykind-projector",
    "-Yexplicit-nulls",
    "-source",
    "future",
    "-Xfatal-warnings"
  ),
  libraryDependencies ++= compileDependencies
)

lazy val root = (project in file("."))
  .settings(settings)
