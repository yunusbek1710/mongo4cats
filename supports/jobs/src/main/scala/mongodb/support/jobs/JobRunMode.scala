package mongodb.support.jobs

import pureconfig._
import pureconfig.generic.semiauto._

sealed trait JobRunMode extends Product with Serializable

object JobRunMode {
  case object Once extends JobRunMode
  case object Forever extends JobRunMode
  implicit val configReader: ConfigReader[JobRunMode] = deriveEnumerationReader[JobRunMode]

}
