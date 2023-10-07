name         := "services"
scalaVersion := "2.13.12"

lazy val services_mongo4cats = project.in(file("mongo4cats"))

aggregateProjects(services_mongo4cats)
