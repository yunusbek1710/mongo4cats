package services.mongo

import mongo4cats.operations.Filter
import mongodb.support.database.DBSuite
import services.mongo.domain.User

object UserRepositorySpec extends DBSuite with UserGenerator {
  test("User create") { client =>
    for {
      database <- client.getDatabase("test")
      coll <- database.getCollectionWithCodec[User]("users")
      user = userGen.get
      _ <- coll.insertOne(user)
      userInDb <- coll.find(Filter.eq("id", user.id)).first
    } yield assert.same(Some(user), userInDb)
  }
}
