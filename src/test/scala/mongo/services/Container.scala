package mongo.services

import cats.effect.{IO, Resource}
import mongo4cats.client.MongoClient
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.noop.NoOpLogger

trait Container {
  type Res
  lazy val imageName: String = "mongo:4.0.10"
  lazy val container: MongoDBContainer = new MongoDBContainer(
    DockerImageName.parse(imageName)
  )

  implicit val logger: SelfAwareStructuredLogger[IO] = NoOpLogger[IO]

  val dbResource: Resource[IO, MongoClient[IO]] =
    for {
      container <- Resource.fromAutoCloseable {
        IO {
          container.start()
          container
        }
      }
      url = container.getReplicaSetUrl("test")

      client <- MongoClient.fromConnectionString[IO](url)
      _ <- Resource.eval(logger.info("Container has started"))
    } yield client
}
