package mongodb.database

import eu.timepit.refined.types.net.UserPortNumber
import eu.timepit.refined.types.string.NonEmptyString

case class MongoConfig(
    host: NonEmptyString,
    port: Option[UserPortNumber],
    user: NonEmptyString,
    password: NonEmptyString,
    database: NonEmptyString,
  )
