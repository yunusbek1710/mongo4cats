package mongodb.support.database

import cats.effect.IO
import weaver.Expectations

trait TestCase {
  def check: IO[Expectations]
}
