package mongo.syntax

import cats.MonadThrow
import cats.data.EitherT
import cats.implicits.catsSyntaxFlatMapOps
import io.circe._
import io.circe.parser.decode
import org.typelevel.log4cats.Logger

trait CirceSyntax {
  implicit def circeSyntaxDecoderOps(s: String): DecoderOps = new DecoderOps(s)
  implicit def circeSyntaxJsonDecoderOps(json: Json): JsonDecoderOps = new JsonDecoderOps(json)

  implicit def mapEncoder[K: Encoder, V: Encoder]: Encoder[Map[K, V]] =
    (map: Map[K, V]) => Encoder[List[(K, V)]].apply(map.toList)

  implicit def mapDecoder[K: Decoder, V: Decoder]: Decoder[Map[K, V]] =
    (c: HCursor) => c.as[List[(K, V)]].map(_.toMap)
}

final class DecoderOps(private val s: String) {
  def asF[F[_]: MonadThrow, A: Decoder]: F[A] =
    EitherT.fromEither[F](decode[A](s)).rethrowT
  def as[A: Decoder]: A = decode[A](s).fold(throw _, json => json)
}
final class JsonDecoderOps(json: Json) {
  def decodeAsF[F[_]: MonadThrow, A](implicit decoder: Decoder[A]): F[A] =
    EitherT.fromEither[F](decoder.decodeJson(json)).rethrowT
  def decodeAsEitherF[F[_]: MonadThrow, A, B](
      implicit
      decoderA: Decoder[A],
      decoderB: Decoder[B],
      logger: Logger[F],
    ): F[Either[B, A]] =
    EitherT
      .fromEither[F](decoderA.decodeJson(json))
      .leftSemiflatMap { err =>
        logger.warn(s"error: $err") >>
          decodeAsF[F, B]
      }
      .value
}
