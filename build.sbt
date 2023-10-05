import Dependencies.Libraries

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val CompileAndTest = "compile->compile;test->test"

lazy val `test-tools` = project
  .in(file("test"))
  .settings(
    name := "test-tools",
    libraryDependencies ++= Libraries.Testing.all,
  )
  .dependsOn(common)

lazy val common = project
  .in(file("common"))
  .settings(
    name := "common",
    libraryDependencies ++=
      Libraries.Derevo.all ++
        Libraries.Logging.all ++
        Libraries.Cats.all ++
        Libraries.Refined.all ++
        Libraries.Circe.all ++
        Libraries.Ciris.all ++
        Libraries.Enumeratum.all ++
        Libraries.FS2.all ++
        Libraries.PureConfig.all ++
        Libraries.MongoDB.all ++
        Seq(
          Libraries.newtype,
          Libraries.`monocle-core`,
        ),
  )

lazy val root = (project in file("."))
  .settings(
    name := "mongo4cats"
  )
  .dependsOn(LocalProject("common"), LocalProject("test-tools") % CompileAndTest)

libraryDependencies ++= Libraries.MongoDB.all ++ Libraries.Cats.all ++ Libraries.Refined.all
