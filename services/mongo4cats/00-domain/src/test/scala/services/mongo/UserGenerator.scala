package services.mongo

import mongo.test.generators.Generators
import org.scalacheck.Gen
import services.mongo.domain.User

trait UserGenerator extends Generators {
  def userGen: Gen[User] =
    for {
      id <- intGen
      name <- nonEmptyStringGen(5, 6)
    } yield User(id, name)
}
