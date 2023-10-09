package services.mongo.core.repository

import cats.effect.Async
import cats.implicits.toFunctorOps
import mongo4cats.collection.GenericMongoCollection
import mongo4cats.operations.Filter
import mongo4cats.operations.Update
import services.mongo.domain.User

trait UserRepository[F[_]] {
  def insert(user: User): F[Unit]
  def get: F[List[User]]
  def update(user: User): F[Unit]
  def delete(userId: Int): F[Unit]
}

object UserRepository {
  def make[F[_]: Async](
      collection: GenericMongoCollection[F, User, fs2.Stream[F, *]]
    ): UserRepository[F] =
    new UserRepository[F] {
      override def insert(user: User): F[Unit] =
        collection.insertOne(user).void

      override def get: F[List[User]] =
        collection.find(Filter.empty).all.map(_.toList)

      override def update(user: User): F[Unit] =
        collection
          .updateOne(
            Filter.eq("id", user.id),
            Update
              .set("firstname", user.firstname)
              .set("lastname", user.lastname)
              .set("age", user.age)
              .set("phone", user.phone),
          )
          .void

      override def delete(userId: Int): F[Unit] =
        collection.deleteOne(Filter.eq("id", userId)).void
    }
}
