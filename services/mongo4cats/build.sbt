import Dependencies.Libraries

name := "mongo"

lazy val `services_mongo-domain` = project
  .in(file("00-domain"))
  .settings(
    libraryDependencies ++= Libraries.MongoDB.all ++ Seq(
      Libraries.chimney
    )
  )
  .dependsOn(
    LocalProject("common"),
    LocalProject("test-tools") % CompileAndTest,
  )

lazy val `services_mongo-core` =
  project
    .in(file("02-core"))
    .dependsOn(
      LocalProject("support_database") % CompileAndTest,
      `services_mongo-domain`          % CompileAndTest,
    )

lazy val `services_mongo-jobs` =
  project
    .in(file("03-jobs"))
    .dependsOn(`services_mongo-core`, LocalProject("support_jobs"))

lazy val `services_mongo-server` =
  project
    .in(file("03-server"))
    .dependsOn(`services_mongo-core`)

lazy val `services_mongo-runner` =
  project
    .in(file("04-runner"))
    .dependsOn(
      `services_mongo-jobs`,
      `services_mongo-server`,
    )
    .settings(
      libraryDependencies ++= Seq(
        Libraries.GRPC.server
      )
    )
    .settings(DockerImagePlugin.serviceSetting("mongo"))
    .enablePlugins(DockerImagePlugin, JavaAppPackaging, DockerPlugin)

aggregateProjects(
  `services_mongo-domain`,
  `services_mongo-core`,
  `services_mongo-jobs`,
  `services_mongo-server`,
  `services_mongo-runner`,
)
