package mongodb.support.jobs

import cats.Monad
import cats.syntax.all._
import org.typelevel.log4cats.Logger

import scala.concurrent.duration.FiniteDuration

/** Entity responsible for monitoring job execution -
  * storing job's state information such as last execution time
  */
trait SupervisedPeriodicJob[F[_], Env] {
  /* Supervised job */
  def job: PeriodicJob[F, Env]
}

object SupervisedPeriodicJob {
  def make[F[_]: Monad, Env](
      periodicJob: PeriodicJob[F, Env]
    )(implicit
      calendar: Calendar[F]
    ): SupervisedPeriodicJob[F, Env] =
    new SupervisedPeriodicJob[F, Env] {
      val job: PeriodicJob[F, Env] = new PeriodicJob[F, Env] {
        def name: String = periodicJob.name
        def interval: FiniteDuration = periodicJob.interval
        def logger: Logger[F] = periodicJob.logger

        def run(env: Env): F[Unit] =
          for {
            _ <- periodicJob.run(env)
          } yield ()
      }
    }
}
