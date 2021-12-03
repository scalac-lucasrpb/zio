name := "zio"

version := "0.1"

scalaVersion := "3.1.0"

lazy val zioVersion = "1.0.12"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-streams" % zioVersion
)