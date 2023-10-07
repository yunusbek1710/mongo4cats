name         := "supports"
scalaVersion := "2.13.12"

lazy val support_database = project.in(file("database"))
lazy val support_jobs = project.in(file("jobs"))

aggregateProjects(
  support_database,
  support_jobs,
)
