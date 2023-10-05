package mongo.services

import bulavka.test.generators.Generators
import org.scalacheck.Gen
import mongo.test.generators.Generators
import mongodb.domain.User

trait UserGenerator extends Generators {
  def userGen: Gen[User] =
    for {
      id <- intGen
      name <- nonEmptyStringGen(5, 6)
    } yield User(id, name)
}
