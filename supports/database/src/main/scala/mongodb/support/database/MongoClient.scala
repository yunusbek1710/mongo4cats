package mongodb.support.database

import cats.effect.Async
import cats.effect.Resource
import mongo.syntax.refined._
import mongo4cats.client.{ MongoClient => Client }
import mongo4cats.models.client.MongoConnection
import mongo4cats.models.client.MongoConnectionType
import mongo4cats.models.client.MongoCredential

object MongoClient {
  def make[F[_]: Async](config: MongoConfig): Resource[F, Client[F]] =
    Client.fromConnection(
      MongoConnection(
        config.host,
        config.port.map(_.value),
        Some(MongoCredential(config.user, config.password)),
        MongoConnectionType.Classic,
      )
    )
}
