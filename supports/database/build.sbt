import Dependencies.Libraries

name := "database"

libraryDependencies ++= Libraries.MongoDB.all

dependsOn(LocalProject("common"), LocalProject("test-tools") % CompileAndTest)
