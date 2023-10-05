package mongo.services

import cats.effect.{IO, Resource}
import mongo4cats.client.MongoClient
import weaver.IOSuite
import weaver.scalacheck.{CheckConfig, Checkers}
trait DBSuite extends IOSuite with Checkers with Container {
  type Res = MongoClient[IO]

  override def checkConfig: CheckConfig = CheckConfig.default.copy(minimumSuccessful = 1)

  override def sharedResource: Resource[IO, Res] = dbResource
}
