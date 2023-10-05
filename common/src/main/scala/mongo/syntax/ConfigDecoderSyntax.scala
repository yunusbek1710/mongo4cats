package mongo.syntax

import cats.implicits.toBifunctorOps
import io.circe.Decoder
import io.circe.parser.decode

trait ConfigDecoderSyntax {
  implicit def circeConfigDecoder[A: Decoder]: ConfigDecoder[String, A] =
    ConfigDecoder[String].mapEither { (_, s) =>
      decode[A](s).leftMap(error => ConfigError(error.getMessage))
    }

  implicit def listConfigDecoder[A: Decoder]: ConfigDecoder[String, List[A]] =
    circeConfigDecoder[List[A]]
}
