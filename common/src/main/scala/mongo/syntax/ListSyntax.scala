package mongo.syntax

import cats.implicits.catsSyntaxTuple2Semigroupal
import eu.timepit.refined.types.numeric.NonNegInt
import mongo.syntax.refined.commonSyntaxAutoUnwrapV

trait ListSyntax {
  implicit def listSyntaxPaginationOps[A](list: List[A]): PaginationOps[A] =
    new PaginationOps(list)
}

final class PaginationOps[A](list: List[A]) {
  def paginate(maybePage: Option[NonNegInt], maybeLimit: Option[NonNegInt]): List[A] =
    (maybePage, maybeLimit)
      .mapN {
        case page -> limit =>
          val offset: Int = (page - 1) * limit
          list.slice(offset, offset + limit)
      }
      .getOrElse(list)
}
