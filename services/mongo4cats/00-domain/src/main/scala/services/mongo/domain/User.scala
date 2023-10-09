package services.mongo.domain

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import mongo4cats.circe.deriveCirceCodecProvider
import mongo4cats.codecs.MongoCodecProvider

case class User(
    id: Int,
    firstname: String,
    lastname: String,
    age: Int,
    phone: Option[String] = None,
  )

object User {
  implicit val codec: Codec.AsObject[User] = deriveCodec[User]
  implicit val categoryCodecProvided: MongoCodecProvider[User] =
    deriveCirceCodecProvider[User]
}
