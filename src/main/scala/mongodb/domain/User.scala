package mongodb.domain

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import mongo4cats.circe.deriveCirceCodecProvider
import mongo4cats.codecs.MongoCodecProvider

case class User(id: Int, name: String)

object User {
  implicit val codec: Codec.AsObject[User] = deriveCodec[User]
  implicit val categoryCodecProvided: MongoCodecProvider[User] =
    deriveCirceCodecProvider[User]
}
