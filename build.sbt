import Dependencies.Libraries

ThisBuild / scalaVersion := "2.13.12"

name := "mongo4cats"

lazy val root = (project in file("."))
  .aggregate(
    supports,
    services,
    `test-tools`,
  )

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

lazy val supports = project
  .in(file("supports"))
  .settings(
    name := "supports"
  )

lazy val services = project
  .in(file("services"))
  .settings(
    name := "services"
  )

lazy val `test-tools` = project
  .in(file("test"))
  .settings(
    name := "test-tools",
    libraryDependencies ++= Libraries.Testing.all,
  )
  .dependsOn(common)

addCommandAlias(
  "styleCheck",
  "all scalafmtSbtCheck; scalafmtCheckAll; Test / compile; scalafixAll --check",
)

Global / lintUnusedKeysOnLoad := false
Global / onChangedBuildSource := ReloadOnSourceChanges
