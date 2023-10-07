import com.typesafe.sbt.packager.Keys._
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import com.typesafe.sbt.packager.docker.{DockerChmodType, DockerPlugin}
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.{Docker, dockerChmodType}
import sbt.Keys.*
import sbt.{Def, *}

object DockerImagePlugin extends AutoPlugin {
  object autoImport {
    lazy val generateServiceImage: TaskKey[Unit] =
      taskKey[Unit]("Generates an image with the native binary")
  }
  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      dockerBaseImage    := "openjdk:11-jre-slim-buster",
      dockerUpdateLatest := true,
      dockerChmodType    := DockerChmodType.UserGroupWriteExecute,
    )

  def serviceSetting(serviceName: String): Seq[Def.Setting[_]] =
    Seq(
      Docker / packageName         := s"bulavka/service-$serviceName",
      Docker / version             := "latest",
      packageDoc / publishArtifact := false,
      packageSrc / publishArtifact := true,
    )
  def endpointSetting(serviceName: String): Seq[Def.Setting[_]] =
    Seq(
      Docker / packageName         := s"bulavka/endpoint-$serviceName",
      Docker / version             := "latest",
      packageDoc / publishArtifact := false,
      packageSrc / publishArtifact := true,
    )

  override def requires: sbt.Plugins =
    JavaAppPackaging && DockerPlugin
}
