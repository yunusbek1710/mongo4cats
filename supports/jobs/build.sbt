import Dependencies.Libraries

name         := "jobs"
scalaVersion := "2.13.12"

libraryDependencies ++= Seq(
  Libraries.Cron4s.core,
  Libraries.Fs2Cron4s.core,
)

dependsOn(LocalProject("common"))
