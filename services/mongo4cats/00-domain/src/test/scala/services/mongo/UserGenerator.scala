package services.mongo

import mongo.test.generators.Generators
import org.scalacheck.Gen
import services.mongo.domain.User

trait UserGenerator extends Generators {
  def userGen: Gen[User] =
    for {
      id <- intGen
      firstname <- nonEmptyStringGen(5, 6)
      lastname <- nonEmptyStringGen(5, 6)
      age <- intGen
      phone <- nonEmptyStringGen(5, 6).opt
    } yield User(id, firstname, lastname, age, phone)
}
